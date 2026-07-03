package com.gchong3068.xiaohongshu.auth.alarm;

import com.gchong3068.xiaohongshu.auth.alarm.impl.MailAlarmHelper;
import com.gchong3068.xiaohongshu.auth.alarm.impl.SmsAlarmHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gc
 * @date 2024年06月07日 22:13
 */
@Configuration
@RefreshScope
public class AlarmConfig {

    @Value("${alarm.type}")
    private String alarmType;

    @Bean
    @RefreshScope
    public AlarmInterface alarmInterface(){

        if (StringUtils.equals("sms",alarmType)){
            return new SmsAlarmHelper();
        } else if (StringUtils.equals("mail",alarmType)) {
            return new MailAlarmHelper();
        }else {
            throw new IllegalArgumentException("错误的告警类型...");
        }
    }
}
