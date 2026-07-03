package com.gchong3068.xiaohongshu.comment.biz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: Retry 配置读取
 * @Author: gchong3068
 * @Date: 2025/1/1 16:57
 * @Version: v1.0.0
 **/
@ConfigurationProperties(prefix = RetryProperties.PREFIX)
@Component
@Data
public class RetryProperties {
    public static final String PREFIX = "retry";

    /**
     * 最大重试次数
     */
    private Integer maxAttempts = 3;

    /**
     * 初始间隔时间，单位 ms
     */
    private Integer initInterval = 1000;

    /**
     * 乘积（每次乘以 2）
     */
    private Double multiplier = 2.0;
}
