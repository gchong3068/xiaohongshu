package com.gchong3068.xiaohongshu.auth.fantory;

import com.gchong3068.xiaohongshu.auth.strategy.SmsStrategy;
import com.gchong3068.xiaohongshu.auth.strategy.impl.AliyunSmsStrategy;
import com.gchong3068.xiaohongshu.auth.strategy.impl.ZhenziyunSmsStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 短信策略工厂
 * @Author: gchong3068
 * @Date: 2024/6/28 20:14
 * @Version: v1.0.0
 **/
@Configuration
@RefreshScope
public class SmsStrategyFactory {
    @Value("${sms.type}")
    private String smsType;


    @Bean
    @RefreshScope
    public SmsStrategy getSmsStrategy(){
        if (StringUtils.equals(smsType,"zhenziyun")){
            return new ZhenziyunSmsStrategy();
        } else if (StringUtils.equals(smsType,"aliyun")) {
            return new AliyunSmsStrategy();
        }
        throw new RuntimeException("短信类型错误");
    }
}
