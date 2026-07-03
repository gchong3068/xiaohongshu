package com.gchong3068.xiaohongshu.auth.alarm.impl;

import com.gchong3068.xiaohongshu.auth.alarm.AlarmInterface;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gc
 * @date 2024年06月07日 22:11
 */
@Slf4j
public class SmsAlarmHelper implements AlarmInterface {
    /**
     * 发送告警信息
     * @author gc
     * @date 2024/6/7 22:11
     * @param message 错误信息
     * @return boolean
     */
    @Override
    public boolean send(String message) {
        log.info("==> 【短信告警】：{}", message);
        return true;
    }
}
