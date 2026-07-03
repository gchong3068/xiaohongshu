package com.gchong3068.xiaohongshu.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gc
 * @date 2024年05月23日 18:57
 */
@Configuration
public class ThreadPoolConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(10);
        //最大线程数
        executor.setMaxPoolSize(50);
        //队列容量
        executor.setQueueCapacity(200);
        //存活时间
        executor.setKeepAliveSeconds(30);
        //线程前缀
        executor.setThreadNamePrefix("AuthExecutor-");

        //拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        //等待所有任务结束后关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);

        executor.setAwaitTerminationSeconds(60);

        executor.initialize();
        return executor;
    }
}
