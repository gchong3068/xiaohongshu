package com.gchong3068.xiaohongshu.auth.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.gchong3068.xiaohongshu.auth.alarm.AlarmInterface;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gc
 * @date 2024年06月07日 22:03
 */
@RestController
@Slf4j
public class TestController {

    @NacosValue(value = "${rate-limit.api.limit}" , autoRefreshed = true)
    private Integer limit;

    @Resource
    private AlarmInterface alarm;

    @GetMapping("/test")
    public String test(){
        return "当前限流阈值为：" + limit;
    }


    @GetMapping("/alarm")
    public String sendAlarm(){
        alarm.send("系统出错了");
        return "alarm success";
    }

}
