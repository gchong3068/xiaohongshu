package com.gchong3068.xiaohongshu.gateway;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gc
 * @date 2024年06月13日 19:01
 */
@SpringBootApplication
@EnableEncryptableProperties
public class XiaohongshuGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(XiaohongshuGatewayApplication.class, args);
    }
}
