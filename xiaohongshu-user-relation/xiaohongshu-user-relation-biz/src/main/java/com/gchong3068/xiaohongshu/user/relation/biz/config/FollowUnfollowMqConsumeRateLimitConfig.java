package com.gchong3068.xiaohongshu.user.relation.biz.config;

import com.alibaba.nacos.shaded.com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 关注/取关消息消费限流配置
 * @Author: gchong3068
 * @Date: 2024/9/18 20:23
 * @Version: v1.0.0
 **/
@Configuration
@RefreshScope
public class FollowUnfollowMqConsumeRateLimitConfig {

    @Value("${mq-consumer.follow-unfollow.rate-limit}")
    private double rateLimit;

    @Bean
    @RefreshScope
    public RateLimiter rateLimiter(){
        return RateLimiter.create(rateLimit);
    }
}
