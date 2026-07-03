package com.gchong3068.framework.biz.context.config;

import com.gchong3068.framework.biz.context.filter.HeaderUserId2ContextFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * @Description: 上下文配置自动配置
 * @Author: gchong3068
 * @Date: 2024/6/24 17:26
 * @Version: v1.0.0
 **/
@AutoConfiguration
public class ContextAutoConfiguration {
    @Bean
    public FilterRegistrationBean<HeaderUserId2ContextFilter> filterFilterRegistrationBean(){
        HeaderUserId2ContextFilter filter = new HeaderUserId2ContextFilter();
        return new FilterRegistrationBean<>(filter);
    }
}
