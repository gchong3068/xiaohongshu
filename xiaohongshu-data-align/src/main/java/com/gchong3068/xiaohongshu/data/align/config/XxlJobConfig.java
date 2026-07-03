package com.gchong3068.xiaohongshu.data.align.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: xxl-job配置
 * @Author: gchong3068
 * @Date: 2024/11/7 13:56
 * @Version: v1.0.0
 **/
@Configuration
@Slf4j
public class XxlJobConfig {

    @Resource
    private XxlJobProperties xxlJobProperties;


    @Bean
    public XxlJobSpringExecutor xxlJobSpringExecutor(){
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getAppName());
        xxlJobSpringExecutor.setIp(xxlJobProperties.getIp());
        xxlJobSpringExecutor.setPort(xxlJobProperties.getPort());
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());
        return xxlJobSpringExecutor;
    }

}
