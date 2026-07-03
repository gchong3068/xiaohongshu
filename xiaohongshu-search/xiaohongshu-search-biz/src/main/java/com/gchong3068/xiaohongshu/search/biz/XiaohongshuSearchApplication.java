package com.gchong3068.xiaohongshu.search.biz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description: 启动类
 * @Author: gchong3068
 * @Date: 2024/12/2 18:36
 * @Version: v1.0.0
 **/
@SpringBootApplication
@EnableScheduling
public class XiaohongshuSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(XiaohongshuSearchApplication.class);
    }
}
