package com.gchong3068.xiaohongshu.count.biz.config;

import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Description: RocketMQ配置
 * @Author: gchong3068
 * @Date: 2024/9/30 21:00
 * @Version: v1.0.0
 **/
@Configuration
@Import(RocketMQAutoConfiguration.class)
public class RocketMQConfig {
}
