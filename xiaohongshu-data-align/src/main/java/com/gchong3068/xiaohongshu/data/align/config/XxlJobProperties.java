package com.gchong3068.xiaohongshu.data.align.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: xxl-job配置读取
 * @Author: gchong3068
 * @Date: 2024/11/7 13:57
 * @Version: v1.0.0
 **/
@Data
@Component
@ConfigurationProperties(prefix = XxlJobProperties.PREFIX)
public class XxlJobProperties {
    public static final String PREFIX = "xxl.job";

    private String adminAddresses;

    private String accessToken;

    private String appName;

    private String ip;

    private int port;

    private String logPath;

    private int logRetentionDays = 30;
}
