package com.gchong3068.xiaohongshu.data.align;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description: 数据对齐服务启动类
 * @Author: gchong3068
 * @Date: 2024/11/4 18:39
 * @Version: v1.0.0
 **/
@SpringBootApplication
@MapperScan("com.gchong3068.xiaohongshu.data.align.domain.mapper")
@EnableEncryptableProperties
@EnableFeignClients(basePackages = "com.gchong3068.xiaohongshu")
public class XiaohongshuDataAlignApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(XiaohongshuDataAlignApplication.class, args);
    }
}
