package com.gchong3068.xiaohongshu.user.biz.consumer;

import com.gchong3068.xiaohongshu.user.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.user.biz.constant.RedisKeyConstants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @Description: 延时删除 Redis 笔记缓存
 * @Author: gchong3068
 * @Date: 2025/6/10 18:04
 * @Version: v1.0.0
 **/
@Component
@Slf4j
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_" + MQConstants.TOPIC_DELAY_DELETE_USER_REDIS_CACHE, // Group
        topic = MQConstants.TOPIC_DELAY_DELETE_USER_REDIS_CACHE // 消费的主题 Topic)
        )
public class DelayDeleteUserRedisCacheConsumer implements RocketMQListener<String> {

    @Resource
    private RedisTemplate<String , Object> redisTemplate;

    @Override
    public void onMessage(String body) {
        Long userId = Long.valueOf(body);
        log.info("## 延迟消息消费成功, userId: {}", userId);

        // 删除 Redis 用户缓存
        String userInfoRedisKey = RedisKeyConstants.buildUserInfoKey(userId);
        String userProfileRedisKey = RedisKeyConstants.buildUserProfileKey(userId);
        // 批量删除
        redisTemplate.delete(Arrays.asList(userInfoRedisKey, userProfileRedisKey));
    }
}
