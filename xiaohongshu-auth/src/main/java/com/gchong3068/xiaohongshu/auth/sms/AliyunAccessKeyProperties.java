package com.gchong3068.xiaohongshu.auth.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author gc
 * @date 2024年05月24日 19:20
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun")
public class AliyunAccessKeyProperties {
    private String accessKeyId;
    private String accessKeySecret;
}
