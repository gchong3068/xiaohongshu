package com.gchong3068.xiaohongshu.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Description: redis 配置
 * @Author: gchong3068
 * @Date: 2024/6/17 10:44
 * @Version: v1.0.0
 **/
@Configuration
public class RedisTemplateConfig {

    @Bean
    public RedisTemplate<String ,Object> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String ,Object> redisTemplate = new RedisTemplate<>();
        //设置redis连接工厂
        redisTemplate.setConnectionFactory(connectionFactory);

        //使用StringRedisSerializer 序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
