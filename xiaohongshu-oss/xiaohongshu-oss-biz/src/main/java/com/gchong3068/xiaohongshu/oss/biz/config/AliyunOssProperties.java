package com.gchong3068.xiaohongshu.oss.biz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: 阿里云 OSS 配置项
 * @Author: gchong3068
 * @Date: 2024/6/29 22:55
 * @Version: v1.0.0
 **/
@ConfigurationProperties(prefix = "storage.aliyun-oss")
@Component
@Data
public class AliyunOssProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
}
