package com.gchong3068.xiaohongshu.count.biz.consumer;

import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.count.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.count.biz.constant.RedisKeyConstants;
import com.gchong3068.xiaohongshu.count.biz.enums.LikeUnlikeCommentTypeEnum;
import com.gchong3068.xiaohongshu.count.biz.model.dto.AggregationCountLikeUnLikeCommentMqDTO;
import com.gchong3068.xiaohongshu.count.biz.model.dto.CountLikeUnlikeCommentMqDTO;
import com.github.phantomthief.collection.BufferTrigger;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 计数 ：评论点赞数
 * @Author: gchong3068
 * @Date: 2025/5/29 17:51
 * @Version: v1.0.0
 **/
@Component
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_count_" + MQConstants.TOPIC_COMMENT_LIKE_OR_UNLIKE,
        topic = MQConstants.TOPIC_COMMENT_LIKE_OR_UNLIKE)
@Slf4j
public class CountCommentLikeConsumer implements RocketMQListener<String> {

    private BufferTrigger<String> bufferTrigger = BufferTrigger.<String>batchBlocking()
            .bufferSize(50000)
            .batchSize(1000)
            .linger(Duration.ofSeconds(1))
            .setConsumerEx(this::consumeMessage)
            .build();

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    @Override
    public void onMessage(String body) {
        bufferTrigger.enqueue(body);
    }

    private void consumeMessage(List<String> bodys) {
        log.info("==> 【评论点赞数】聚合消息, size: {}", bodys.size());
        log.info("==> 【评论点赞数】聚合消息, {}", JsonUtils.toJsonString(bodys));

        List<CountLikeUnlikeCommentMqDTO> countLikeUnlikeCommentMqDTOS = bodys.stream()
                .map(body -> JsonUtils.parseObject(body, CountLikeUnlikeCommentMqDTO.class)).toList();

        //按评论Id 进行分类
        Map<Long, List<CountLikeUnlikeCommentMqDTO>> groupMap = countLikeUnlikeCommentMqDTOS.stream()
                .collect(Collectors.groupingBy(CountLikeUnlikeCommentMqDTO::getCommentId));

        List<AggregationCountLikeUnLikeCommentMqDTO> countList =  Lists.newArrayList();

        for (Map.Entry<Long, List<CountLikeUnlikeCommentMqDTO>> entry : groupMap.entrySet()){
            //评论ID
            Long commentId = entry.getKey();

            List<CountLikeUnlikeCommentMqDTO> list = entry.getValue();

            int finalCount = 0;
            for (CountLikeUnlikeCommentMqDTO countLikeUnlikeCommentMqDTO :list){
                //获取操作类型
                Integer type = countLikeUnlikeCommentMqDTO.getType();

                LikeUnlikeCommentTypeEnum likeUnlikeCommentTypeEnums = LikeUnlikeCommentTypeEnum.valueOf(type);

                if (Objects.isNull(likeUnlikeCommentTypeEnums)) continue;

                switch(likeUnlikeCommentTypeEnums){
                    case LIKE -> finalCount += 1;
                    case UNLIKE -> finalCount -= 1;
                }
            }

            countList.add(AggregationCountLikeUnLikeCommentMqDTO.builder()
                            .commentId(commentId)
                            .count(finalCount)
                    .build());
        }

        log.info("## 【评论点赞数】聚合后的计数数据: {}", JsonUtils.toJsonString(countList));

        //更新Redis
        countList.forEach(item ->{
            Long commentId = item.getCommentId();
            //聚合后的计数
            Integer count = item.getCount();

            String countCommentRedisKey = RedisKeyConstants.buildCountCommentKey(commentId);
            Boolean isCountCommentExisted = redisTemplate.hasKey(countCommentRedisKey);

            if (isCountCommentExisted){
                redisTemplate.opsForHash().increment(countCommentRedisKey,RedisKeyConstants.FIELD_LIKE_TOTAL,count);
            }
        });

        //发送MQ
        Message<String> message = MessageBuilder.withPayload(JsonUtils.toJsonString(countList)).build();

        rocketMQTemplate.asyncSend(MQConstants.TOPIC_COUNT_COMMENT_LIKE_2_DB, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("==> 【计数服务：评论点赞数写库】MQ 发送成功，SendResult: {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("==> 【计数服务：评论点赞数写库】MQ 发送异常: ", throwable);
            }
        });
    }
}
