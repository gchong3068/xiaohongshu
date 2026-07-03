package com.gchong3068.xiaohongshu.note.biz;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * @Description:
 * @Author: gchong3068
 * @Date: 2024/8/13 21:58
 * @Version: v1.0.0
 **/
@SpringBootApplication
@MapperScan("com.gchong3068.xiaohongshu.note.biz.domain.mapper")
@EnableFeignClients(basePackages = "com.gchong3068.xiaohongshu")
@EnableEncryptableProperties
public class XiaohongshuNoteBizApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaohongshuNoteBizApplication.class ,args);
    }
}
