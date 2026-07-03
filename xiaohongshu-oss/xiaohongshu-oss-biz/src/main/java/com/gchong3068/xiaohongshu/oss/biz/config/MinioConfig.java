package com.gchong3068.xiaohongshu.oss.biz.config;

import io.minio.MinioClient;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: Minio 配置
 * @Author: gchong3068
 * @Date: 2024/6/28 21:29
 * @Version: v1.0.0
 **/
@Configuration
public class MinioConfig {

    @Resource
    private MinioProperties minioProperties;


    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(),minioProperties.getSecretKey())
                .build();
    }
}
