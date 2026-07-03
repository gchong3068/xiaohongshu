package com.gchong3068.xiaohongshu.user.relation.biz;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description: 用户关系服务启动类
 * @Author: gchong3068
 * @Date: 2024/9/5 20:08
 * @Version: v1.0.0
 **/
@SpringBootApplication
@MapperScan("com.gchong3068.xiaohongshu.user.relation.biz.domain.mapper")
@EnableFeignClients("com.gchong3068.xiaohongshu")
@EnableEncryptableProperties
public class XiaohongshuUserRelationBizApplication {
    public static void main(String[] args) {

        SpringApplication.run(XiaohongshuUserRelationBizApplication.class, args);
    }
}
