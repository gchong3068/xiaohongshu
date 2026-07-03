package com.gchong3068.xiaohongshu.count.biz.consumer;

import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.count.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.count.biz.constant.RedisKeyConstants;
import com.gchong3068.xiaohongshu.count.biz.enums.FollowUnfollowTypeEnum;
import com.gchong3068.xiaohongshu.count.biz.model.dto.CountFollowUnfollowMqDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Description: 关注 计数
 * @Author: gchong3068
 * @Date: 2024/10/8 21:38
 * @Version: v1.0.0
 **/
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_" + MQConstants.TOPIC_COUNT_FOLLOWING,
        topic = MQConstants.TOPIC_COUNT_FOLLOWING
    )
public class CountFollowingConsumer implements RocketMQListener<String> {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private RocketMQTemplate rocketMQTemplate;


    @Override
    public void onMessage(String item) {
        log.info("## 消费 MQ 【计数：关注数】, {}...", item);

        if (StringUtils.isBlank(item)) return;

        // 关注数和粉丝数计数场景不同，单个用户无法短时间内关注大量用户，所以无需聚合
        // 直接对 Redis 中的 Hash 进行 +1 或 -1 操作即可

        CountFollowUnfollowMqDTO countFollowUnfollowMqDTO = JsonUtils.parseObject(item, CountFollowUnfollowMqDTO.class);

        //操作类型
        Integer type = countFollowUnfollowMqDTO.getType();

        Long userId = countFollowUnfollowMqDTO.getUserId();

        String redisKey = RedisKeyConstants.buildCountUserKey(userId);
        //判断Hash
        boolean isExisted = redisTemplate.hasKey(redisKey);

        if (isExisted){
            //关注+1 ，取关-1
            long count = Objects.equals(type, FollowUnfollowTypeEnum.FOLLOW.getCode()) ? 1 : -1;
            //对Hash followingTotal 进行加减操作
            redisTemplate.opsForHash().increment(redisKey,RedisKeyConstants.FIELD_FOLLOWING_TOTAL,count);
        }


        Message<String> message = MessageBuilder.withPayload(item).build();

        //异步发送Mq
        rocketMQTemplate.asyncSend(MQConstants.TOPIC_COUNT_FOLLOWING_2_DB, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("==> 【计数服务：关注数入库】MQ 发送成功，SendResult: {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("==> 【计数服务：关注数入库】MQ 发送异常: ", throwable);
            }
        });
    }
}
