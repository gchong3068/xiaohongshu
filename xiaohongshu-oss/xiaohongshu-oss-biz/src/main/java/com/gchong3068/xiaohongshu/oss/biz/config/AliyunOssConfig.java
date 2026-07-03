package com.gchong3068.xiaohongshu.oss.biz.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: Aliyun Oss 配置
 * @Author: gchong3068
 * @Date: 2024/6/29 23:00
 * @Version: v1.0.0
 **/
@Configuration
public class AliyunOssConfig {

    @Resource
    private AliyunOssProperties aliyunOssProperties;
    

    /**
     * 构建 阿里云OSS 客户端
     * @Auther: gchong3068
     * @Date: 2024/6/29 23:04 
 	 * @return com.aliyun.oss.OSS
     **/
    @Bean
    public OSS aliyunOSSClient(){
        DefaultCredentialProvider credentialProvider = CredentialsProviderFactory.newDefaultCredentialProvider(
                aliyunOssProperties.getAccessKey(), aliyunOssProperties.getSecretKey()
        );
        
        return new OSSClientBuilder().build(aliyunOssProperties.getEndpoint(), credentialProvider);
    }
}
