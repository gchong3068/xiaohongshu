package com.gchong3068.xiaohongshu.user.biz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description: 线程池
 * @Author: gchong3068
 * @Date: 2024/8/19 22:07
 * @Version: v1.0.0
 **/
@Configuration
public class ThreadPoolConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("UserExecutor-");
        //拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.setWaitForTasksToCompleteOnShutdown(true);
        //等待时间
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();
        return executor;
    }


}
