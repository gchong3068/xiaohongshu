package com.gchong3068.xiaohongshu.comment.biz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @Description: 启动类
 * @Author: gchong3068
 * @Date: 2024/12/23 19:37
 * @Version: v1.0.0
 **/
@SpringBootApplication
@MapperScan("com.gchong3068.xiaohongshu.comment.biz.domain.mapper")
@EnableRetry   // 启用 Spring Retry
@EnableFeignClients(basePackages = "com.gchong3068.xiaohongshu")
public class XiaohongshuCommentBizApplication {
    public static void main(String[] args) {
        SpringApplication.run(XiaohongshuCommentBizApplication.class, args);
    }
}
