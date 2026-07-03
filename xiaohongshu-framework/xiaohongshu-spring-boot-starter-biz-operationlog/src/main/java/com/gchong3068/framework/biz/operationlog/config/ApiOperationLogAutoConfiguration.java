package com.gchong3068.framework.biz.operationlog.config;

import com.gchong3068.framework.biz.operationlog.aspect.ApiOperationLogAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author gc
 * @date 2024年05月05日 20:53
 */
@AutoConfiguration
public class ApiOperationLogAutoConfiguration {

    @Bean
    public ApiOperationLogAspect apiOperationLogAspect(){
        return new ApiOperationLogAspect();
    }
}
