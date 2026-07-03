package com.gchong3068.framework.biz.context.config;

import com.gchong3068.framework.biz.context.interceptor.FeignRequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @Description: Feign 自动装配
 * @Author: gchong3068
 * @Date: 2024/7/7 21:43
 * @Version: v1.0.0
 **/
@AutoConfiguration
public class FeignContextAutoConfiguration {

    @Bean
    public FeignRequestInterceptor feignRequestInterceptor(){
        return new FeignRequestInterceptor();
    }
}
