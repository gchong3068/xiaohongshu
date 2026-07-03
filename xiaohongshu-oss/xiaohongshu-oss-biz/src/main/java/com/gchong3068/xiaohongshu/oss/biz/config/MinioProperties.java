package com.gchong3068.xiaohongshu.oss.biz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: Minio 参数
 * @Author: gchong3068
 * @Date: 2024/6/28 21:27
 * @Version: v1.0.0
 **/
@ConfigurationProperties(prefix = "storage.minio")
@Component
@Data
public class MinioProperties {

    private String endpoint;
    private String accessKey;
    private String secretKey;
}
