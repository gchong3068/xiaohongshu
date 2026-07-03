package com.gchong3068.xiaohongshu.oss.biz;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: 对象储存启动类
 * @Author: gchong3068
 * @Date: 2024/6/27 22:24
 * @Version: v1.0.0
 **/
@SpringBootApplication
@EnableEncryptableProperties
public class XiaohongshuOssBizApplication {
    public static void main(String[] args) {
        SpringApplication.run(XiaohongshuOssBizApplication.class);
    }

}
