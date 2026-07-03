package com.gchong3068.xiaohongshu.user.biz.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: jasypt
 * @Author: gchong3068
 * @Date: 2024/11/1 18:43
 * @Version: v1.0.0
 **/
@Configuration
@EnableEncryptableProperties
@Slf4j
public class JasyptConfig {
    @Bean("jasyptStringEncryptor")
    public StringEncryptor jasyptStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("8m8Jx6I7RqQQukf");  // 设置加密和解密时使用的密码
        config.setAlgorithm("PBEWithMD5AndDES");  // 设置加密算法，使用 PBEWithMD5AndDES
        config.setKeyObtentionIterations("1000");// 设置密钥获取的迭代次数，增加安全性
        config.setPoolSize("1");  // 设置加密器池的大小
        config.setProviderName("SunJCE");  // 设置加密提供者，使用 SunJCE 提供者
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");  // 设置盐生成器的类名
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");  // 设置初始化向量（IV）生成器的类名
        config.setStringOutputType("base64");  // 设置输出字符串的编码类型
        encryptor.setConfig(config);
        return encryptor;
    }
}
