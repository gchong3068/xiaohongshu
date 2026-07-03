package com.gchong3068.xiaohongshu.auth.alarm;

public interface AlarmInterface {

    /**
     * 发送告警信息
     * @author gc
     * @date 2024/6/7 22:08
     * @return boolean
     */
    boolean send(String message);
}
