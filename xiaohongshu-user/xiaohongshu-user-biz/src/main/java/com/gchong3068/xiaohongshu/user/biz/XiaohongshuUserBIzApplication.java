package com.gchong3068.xiaohongshu.user.biz;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description: 用户业务启动类
 * @Author: gchong3068
 * @Date: 2024/7/1 22:33
 * @Version: v1.0.0
 **/
@SpringBootApplication
@MapperScan("com.gchong3068.xiaohongshu.user.biz.domain.mapper")
@EnableFeignClients(basePackages = "com.gchong3068.xiaohongshu")
@EnableEncryptableProperties
public class XiaohongshuUserBIzApplication {
    public static void main(String[] args) {
        SpringApplication.run(XiaohongshuUserBIzApplication.class);
    }
}
