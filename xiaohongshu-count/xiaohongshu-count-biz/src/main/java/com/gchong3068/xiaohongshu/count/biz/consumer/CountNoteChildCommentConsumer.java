package com.gchong3068.xiaohongshu.count.biz.consumer;

import cn.hutool.core.collection.CollUtil;
import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.count.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.count.biz.constant.RedisKeyConstants;
import com.gchong3068.xiaohongshu.count.biz.domain.mapper.CommentDOMapper;
import com.gchong3068.xiaohongshu.count.biz.enums.CommentLevelEnum;
import com.gchong3068.xiaohongshu.count.biz.model.dto.CountPublishCommentMqDTO;
import com.github.phantomthief.collection.BufferTrigger;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 计数：笔记二级评论数
 * @Author: gchong3068
 * @Date: 2025/1/12 15:58
 * @Version: v1.0.0
 **/
@Component
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_child_comment_total" + MQConstants.TOPIC_COUNT_NOTE_COMMENT,
        topic = MQConstants.TOPIC_COUNT_NOTE_COMMENT)
@Slf4j
public class CountNoteChildCommentConsumer implements RocketMQListener<String> {

    @Resource
    private CommentDOMapper commentDOMapper;
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    private BufferTrigger<String> bufferTrigger = BufferTrigger.<String> batchBlocking()
            .bufferSize(50000)
            .batchSize(1000)
            .linger(Duration.ofSeconds(1))
            .setConsumerEx(this::consumeMessage)
            .build();

    @Override
    public void onMessage(String body) {
        bufferTrigger.enqueue(body);
    }

    private void consumeMessage(List<String> bodys) {
        log.info("==> 【笔记二级评论数】聚合消息, size: {}", bodys.size());
        log.info("==> 【笔记二级评论数】聚合消息, {}", JsonUtils.toJsonString(bodys));

        //将聚合后信息体Json --> List<CountPublishCommentMqDTO>
        List<CountPublishCommentMqDTO> countPublishCommentMqDTOList = Lists.newArrayList();
        bodys.forEach(body->{
            try{
                List<CountPublishCommentMqDTO> list = JsonUtils.parseList(body, CountPublishCommentMqDTO.class);
                countPublishCommentMqDTOList.addAll(list);
            }catch (Exception e){
                log.error("",e);
            }
        });

        //过滤二级评论并按照parent_id分组
        Map<Long,List<CountPublishCommentMqDTO>> groupMap = countPublishCommentMqDTOList.stream()
                .filter(commentMqDTO-> Objects.equals(CommentLevelEnum.TWO.getCode(), commentMqDTO.getLevel()))
                .collect(Collectors.groupingBy(CountPublishCommentMqDTO::getParentId));

        //无二级评论
        if (CollUtil.isEmpty(groupMap)) return;
        //循环分组词典
        for (Map.Entry<Long, List<CountPublishCommentMqDTO>> entry : groupMap.entrySet()) {

            Long parentId = entry.getKey();
            //总数
            int count = CollUtil.size(entry.getValue());

            // 更新Redis 缓存中评论计数数据
            String commentCountHashKey = RedisKeyConstants.buildCountCommentKey(parentId);

            Boolean hasKey = redisTemplate.hasKey(commentCountHashKey);

            if (hasKey){
                //累加
                redisTemplate.opsForHash()
                        .increment(commentCountHashKey,RedisKeyConstants.FIELD_CHILD_COMMENT_TOTAL,count);
            }

            //更新一级评论下级评论总数
            commentDOMapper.updateChildCommentTotal(parentId, count);
        }

        //获取字典中所以评论Id
        Set<Long> commentIds = groupMap.keySet();

        //异步发送MQ
        Message<String> message = MessageBuilder.withPayload(JsonUtils.toJsonString(commentIds)).build();

        rocketMQTemplate.asyncSend(MQConstants.TOPIC_COMMENT_HEAT_UPDATE, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("==> 【评论热度值更新】MQ 发送成功，SendResult: {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("==> 【评论热度值更新】MQ 发送异常: ", throwable);
            }
        });

    }
}
