package com.gchong3068.xiaohongshu.comment.biz.consumer;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.nacos.shaded.com.google.common.util.concurrent.RateLimiter;
import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.comment.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.comment.biz.constant.RedisKeyConstants;
import com.gchong3068.xiaohongshu.comment.biz.domain.dataobject.CommentDO;
import com.gchong3068.xiaohongshu.comment.biz.domain.mapper.CommentDOMapper;
import com.gchong3068.xiaohongshu.comment.biz.enums.CommentLevelEnum;
import com.gchong3068.xiaohongshu.comment.biz.model.bo.CommentBO;
import com.gchong3068.xiaohongshu.comment.biz.model.dto.CountPublishCommentMqDTO;
import com.gchong3068.xiaohongshu.comment.biz.model.dto.PublishCommentMqDTO;
import com.gchong3068.xiaohongshu.comment.biz.rpc.KeyValueRpcService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 评论批量写库
 * @Author: gchong3068
 * @Date: 2025/1/2 15:18
 * @Version: v1.0.0
 **/
@Component
@Slf4j
public class Comment2DBConsumer {

    @Value("${rocketmq.name-server}")
    private String namesrvAddr;
    @Resource
    private CommentDOMapper commentDOMapper;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private KeyValueRpcService keyValueRpcService;
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private DefaultMQPushConsumer consumer;

    // 每秒创建 1000 个令牌
    private RateLimiter rateLimiter = RateLimiter.create(1000);


    @Bean
    public DefaultMQPushConsumer mqPushConsumer(CommentDOMapper commentDOMapper) throws MQClientException {
        //Group
        String group = "xiaohongshu_group" + MQConstants.TOPIC_PUBLISH_COMMENT;

        //创建新DefaultMQPushConsumer 实例
        consumer = new DefaultMQPushConsumer(group);

        consumer.setNamesrvAddr(namesrvAddr);

        consumer.subscribe(MQConstants.TOPIC_PUBLISH_COMMENT, "*");

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        consumer.setMessageModel(MessageModel.CLUSTERING);

        consumer.setConsumeMessageBatchMaxSize(30);

        //注册信息监听
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            log.info("==> 本批次消息大小: {}", msgs.size());
            try {
                // 令牌桶流控
                rateLimiter.acquire();

                //Json --> DTO
                List<PublishCommentMqDTO> publishCommentMqDTOS = Lists.newArrayList();
                msgs.forEach(msg -> {
                    String msgJson = new String(msg.getBody());
                    log.info("==> Consumer  -  Received message: {}", msgJson);
                    publishCommentMqDTOS.add(JsonUtils.parseObject(msgJson, PublishCommentMqDTO.class));
                });

                //提取不为空的回复ID
                List<Long> replyCommentIds  = publishCommentMqDTOS.stream()
                        .map(PublishCommentMqDTO::getCommentId)
                        .filter(Objects::nonNull).toList();

                //批量查询相关回复记录
                List<CommentDO> replyCommentDOS = null;
                if (CollUtil.isNotEmpty(replyCommentIds)){
                    replyCommentDOS = commentDOMapper.selectByCommentIds(replyCommentIds);
                }

                //DO --> <评论ID - 评论DO>字典
                Map<Long,CommentDO> commentIdAndCommentDOMap = Maps.newHashMap();
                if (CollUtil.isNotEmpty(replyCommentDOS)){
                    commentIdAndCommentDOMap = replyCommentDOS.stream()
                            .collect(Collectors.toMap(CommentDO::getId, commentDO -> commentDO));
                }

                //DTO --> BO
                List<CommentBO> commentBOS = Lists.newArrayList();
                for (PublishCommentMqDTO publishCommentMqDTO : publishCommentMqDTOS){
                    String imageUrl = publishCommentMqDTO.getImageUrl();
                    CommentBO commentBO = CommentBO.builder()
                            .id(publishCommentMqDTO.getCommentId())
                            .noteId(publishCommentMqDTO.getNoteId())
                            .userId(publishCommentMqDTO.getCreatorId())
                            .isContentEmpty(true)
                            .imageUrl(StringUtils.isBlank(imageUrl) ? "" :imageUrl)
                            .level(CommentLevelEnum.ONE.getCode())
                            .parentId(publishCommentMqDTO.getNoteId())
                            .createTime(publishCommentMqDTO.getCreateTime())
                            .updateTime(publishCommentMqDTO.getCreateTime())
                            .isTop(false)
                            .replyTotal(0L)
                            .likeTotal(0L)
                            .replyCommentId(0L)
                            .replyUserId(0L)
                            .build();

                    //评论内容不为空
                    String content = publishCommentMqDTO.getContent();
                    if (StringUtils.isNotBlank(content)){
                        commentBO.setContentUuid(UUID.randomUUID().toString());
                        commentBO.setIsContentEmpty(false);
                        commentBO.setContent(content);
                    }

                    // 设置评论级别、回复用户 ID (reply_user_id)、父评论 ID (parent_id)
                    Long replyCommentId = publishCommentMqDTO.getReplyCommentId();
                    if (Objects.nonNull(replyCommentId)) {
                        CommentDO replyCommentDO = commentIdAndCommentDOMap.get(replyCommentId);

                        if (Objects.nonNull(replyCommentDO)) {
                            // 若回复的评论 ID 不为空，说明是二级评论
                            commentBO.setLevel(CommentLevelEnum.TWO.getCode());

                            commentBO.setReplyCommentId(publishCommentMqDTO.getReplyCommentId());
                            // 父评论 ID
                            commentBO.setParentId(replyCommentDO.getId());
                            if (Objects.equals(replyCommentDO.getLevel(), CommentLevelEnum.TWO.getCode())) { // 如果回复的评论属于二级评论
                                commentBO.setParentId(replyCommentDO.getParentId());
                            }
                            // 回复的哪个用户
                            commentBO.setReplyUserId(replyCommentDO.getUserId());
                        }
                    }

                    commentBOS.add(commentBO);
                }

                log.info("## 清洗后的 CommentBOS: {}", JsonUtils.toJsonString(commentBOS));

                //编程式事务
                Integer insertedRows =  transactionTemplate.execute(status -> {
                    try{
                        //批量存入评论元数据
                        int count = commentDOMapper.batchInsert(commentBOS);
                        //过滤评论不为空BO
                        List<CommentBO> commentContentNotEmptyBOS = commentBOS.stream()
                                .filter(commentBO -> Boolean.FALSE.equals(commentBO.getIsContentEmpty()))
                                .toList();
                        if (CollUtil.isNotEmpty(commentContentNotEmptyBOS)){
                            keyValueRpcService.batchSaveCommentContent(commentContentNotEmptyBOS);
                        }
                        return count;
                    }catch (Exception e){
                        status.setRollbackOnly();  //回滚
                        log.error("",e);
                        throw e;
                    }
                });

                //如果插入行大于一
                if(Objects.nonNull(insertedRows) && insertedRows > 0){
                    // 构建发送给计数服务的 DTO 集合
                    List<CountPublishCommentMqDTO> countPublishCommentMqDTOS = commentBOS.stream()
                            .map(commentBO  -> CountPublishCommentMqDTO.builder()
                                    .noteId(commentBO.getNoteId())
                                    .commentId(commentBO.getId())
                                    .level(commentBO.getLevel())
                                    .parentId(commentBO.getParentId())
                                    .build())
                            .toList();

                    // 异步发送计数 MQ
                    org.springframework.messaging.Message<String> message = MessageBuilder.withPayload(JsonUtils.toJsonString(countPublishCommentMqDTOS))
                            .build();

                    //同步一级评论到redis 热点评论中
                    syncOneLevelComment2RedisZSet(commentBOS);


                    // 异步发送 MQ 消息
                    rocketMQTemplate.asyncSend(MQConstants.TOPIC_COUNT_NOTE_COMMENT, message, new SendCallback() {
                        @Override
                        public void onSuccess(SendResult sendResult) {
                            log.info("==> 【计数: 评论发布】MQ 发送成功，SendResult: {}", sendResult);
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            log.error("==> 【计数: 评论发布】MQ 发送异常: ", throwable);
                        }
                    });
                }

                // 手动 ACK，告诉 RocketMQ 这批次消息消费成功
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } catch (Exception e) {
                log.error("", e);
                // 手动 ACK，告诉 RocketMQ 这批次消息处理失败，稍后再进行重试
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });

        //启动消费者
        consumer.start();
        return consumer;
    }

    @PreDestroy
    public void destroy(){
        if (Objects.nonNull(consumer)){
            try{
             consumer.shutdown();  //关闭消费者
            }catch (Exception e){
                log.error("", e);
            }
        }
    }


    private void syncOneLevelComment2RedisZSet(List<CommentBO> commentBOS){
        Map<Long,List<CommentBO>> commentIdAndBOListMap = commentBOS.stream()
                .filter(commentBO -> Objects.equals(commentBO.getLevel() , CommentLevelEnum.ONE.getCode()))
                .collect(Collectors.groupingBy(CommentBO::getNoteId));

        //循环字典
        commentIdAndBOListMap.forEach((noteId , commentBOList) -> {
            //构建 Redis 热点Zset key
            String key = RedisKeyConstants.buildCommentListKey(noteId);

            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            //lua脚本路径
            script.setScriptSource(new ResourceScriptSource(new ClassPathResource("/lua/add_hot_comment.lua")));
            //返回值类型
            script.setResultType(Long.class);

            //执行 ARGS 参数
            List<Object> args = Lists.newArrayList();

            commentBOList.forEach(commentBO -> {
                args.add(commentBO.getId());
                args.add(0);
            });
            //执行脚本
            redisTemplate.execute(script, Collections.singletonList(key), args.toArray());
        });
    }
}
