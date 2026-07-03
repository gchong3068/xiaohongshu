package com.gchong3068.xiaohongshu.oss.config;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: Feign 表单配置类
 * @Author: gchong3068
 * @Date: 2024/7/5 20:29
 * @Version: v1.0.0
 **/
@Configuration
public class FeignFormConfig {
    @Bean
    public Encoder feignFormEncoder(){
        return new SpringFormEncoder();
    }
}
