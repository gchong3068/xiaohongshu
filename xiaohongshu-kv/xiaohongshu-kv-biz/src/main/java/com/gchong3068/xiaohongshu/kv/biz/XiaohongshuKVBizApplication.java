package com.gchong3068.xiaohongshu.kv.biz;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: 启动类
 * @Author: gchong3068
 * @Date: 2024/7/25 21:33
 * @Version: v1.0.0
 **/
@SpringBootApplication
@EnableEncryptableProperties
public class XiaohongshuKVBizApplication {
    public static void main(String[] args) {
        SpringApplication.run(XiaohongshuKVBizApplication.class,args);
    }
}
