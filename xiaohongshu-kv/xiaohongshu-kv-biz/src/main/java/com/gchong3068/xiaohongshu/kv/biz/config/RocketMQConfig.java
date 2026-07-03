package com.gchong3068.xiaohongshu.kv.biz.config;

import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Description: RocketMQ 配置
 * @Author: gchong3068
 * @Date: 2025/6/11 23:56
 * @Version: v1.0.0
 **/
@Configuration
@Import(RocketMQAutoConfiguration.class)
public class RocketMQConfig {
}
