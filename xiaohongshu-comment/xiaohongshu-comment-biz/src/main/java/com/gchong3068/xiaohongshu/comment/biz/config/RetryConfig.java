package com.gchong3068.xiaohongshu.comment.biz.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * @Description: Retry 配置类
 * @Author: gchong3068
 * @Date: 2025/1/1 16:58
 * @Version: v1.0.0
 **/
@Configuration
public class RetryConfig {

    @Resource
    private RetryProperties retryProperties;


    @Bean
    public RetryTemplate retryTemplate(){
        RetryTemplate retryTemplate = new RetryTemplate();

        // 定义重试策略（最多重试 3 次）
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(retryProperties.getMaxAttempts()); // 最大重试次数

        // 定义间隔策略
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(retryProperties.getInitInterval()); // 初始间隔 2000ms
        backOffPolicy.setMultiplier(retryProperties.getMultiplier());       // 每次乘以 2

        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }

}
