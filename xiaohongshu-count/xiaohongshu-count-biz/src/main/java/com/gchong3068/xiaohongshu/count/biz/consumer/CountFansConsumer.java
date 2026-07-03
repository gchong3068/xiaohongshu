package com.gchong3068.xiaohongshu.count.biz.consumer;

import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.count.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.count.biz.constant.RedisKeyConstants;
import com.gchong3068.xiaohongshu.count.biz.enums.FollowUnfollowTypeEnum;
import com.gchong3068.xiaohongshu.count.biz.model.dto.CountFollowUnfollowMqDTO;
import com.github.phantomthief.collection.BufferTrigger;
import com.google.common.collect.Maps;
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
import java.util.stream.Collectors;

/**
 * @Description: 粉丝 计数
 * @Author: gchong3068
 * @Date: 2024/10/8 21:38
 * @Version: v1.0.0
 **/
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_" + MQConstants.TOPIC_COUNT_FANS,
        topic = MQConstants.TOPIC_COUNT_FANS
    )
public class CountFansConsumer implements RocketMQListener<String> {

    @Resource
    private RedisTemplate<String , Object> redisTemplate;
    @Resource
    private RocketMQTemplate rocketMQTemplate;


    private BufferTrigger<String> bufferTrigger = BufferTrigger.<String>batchBlocking()
            .bufferSize(50000)   //缓存队列最大容量
            .batchSize(1000)     //一批最多聚合
            .linger(Duration.ofSeconds(1))  //聚合时间 1s
            .setConsumerEx(this::consumeMessage)
            .build();

    @Override
    public void onMessage(String item) {
        bufferTrigger.enqueue(item);
    }

    private void consumeMessage(List<String> items) {
        log.info("==> 聚合消息, size: {}", items.size());
        log.info("==> 聚合消息, {}", JsonUtils.toJsonString(items));

        // 解析消息
        List<CountFollowUnfollowMqDTO> countFollowUnfollowMqDTOS = items.stream()
                .map(item -> JsonUtils.parseObject(item, CountFollowUnfollowMqDTO.class)).toList();

        Map<Long,List<CountFollowUnfollowMqDTO>> groupMqp = countFollowUnfollowMqDTOS.stream()
                .collect(Collectors.groupingBy(CountFollowUnfollowMqDTO::getTargetUserId));

        //按组汇总
        Map<Long,Integer> countMap = Maps.newHashMap();

        for (Map.Entry<Long, List<CountFollowUnfollowMqDTO>> entry : groupMqp.entrySet()){
            List<CountFollowUnfollowMqDTO> list = entry.getValue();

            int finalCount = 0;
            for (CountFollowUnfollowMqDTO countFollowUnfollowMqDTO :list){
                //操作类型
                Integer type = countFollowUnfollowMqDTO.getType();

                FollowUnfollowTypeEnum followUnfollowTypeEnum = FollowUnfollowTypeEnum.valueOf(type);

                if (Objects.isNull(followUnfollowTypeEnum)) continue;

                switch(followUnfollowTypeEnum){
                    case FOLLOW -> finalCount += 1;
                    case UNFOLLOW -> finalCount -= 1;
                }
            }
            countMap.put(entry.getKey(), finalCount);
        }

        log.info("## 聚合后的计数数据: {}", JsonUtils.toJsonString(countMap));

        countMap.forEach((k,v) ->{
            String redisKey = RedisKeyConstants.buildCountUserKey(k);
            //判断Redis中 Hash 是否存在
            boolean isExisted = redisTemplate.hasKey(redisKey);

            if (isExisted){
                redisTemplate.opsForHash().increment(redisKey,RedisKeyConstants.FIELD_FANS_TOTAL,v);
            }
        });

        //发送MQ 数据库落库

        Message<String> message = MessageBuilder.withPayload(JsonUtils.toJsonString(countMap)).build();

        //异步发送MQ
        rocketMQTemplate.asyncSend(MQConstants.TOPIC_COUNT_FANS_2_DB, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("==> 【计数服务：粉丝数入库】MQ 发送成功，SendResult: {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("==> 【计数服务：粉丝数入库】MQ 发送异常: ", throwable);
            }
        });
    }

}
