package com.gchong3068.xiaohongshu.oss.biz.fantory;

import com.gchong3068.xiaohongshu.oss.biz.strategy.FileStrategy;
import com.gchong3068.xiaohongshu.oss.biz.strategy.impl.AliyunOssFileStrategy;
import com.gchong3068.xiaohongshu.oss.biz.strategy.impl.MinioFileStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 文件策略工厂
 * @Author: gchong3068
 * @Date: 2024/6/28 12:15
 * @Version: v1.0.0
 **/
@Configuration
@RefreshScope
public class FileStrategyFactory {

    @Value("${storage.type}")
    private String strategyType;


    @Bean
    @RefreshScope
    public FileStrategy getFileStrategy(){
        if (StringUtils.equals(strategyType,"minio")){
            return new MinioFileStrategy();
        } else if (StringUtils.equals(strategyType,"aliyun")) {
            return new AliyunOssFileStrategy();
        }
        throw new IllegalArgumentException("不可用的储存类型");
    }
}
