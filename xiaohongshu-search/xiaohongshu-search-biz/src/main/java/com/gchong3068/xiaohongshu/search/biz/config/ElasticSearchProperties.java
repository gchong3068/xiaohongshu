package com.gchong3068.xiaohongshu.search.biz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: elasticsearch 配置属性
 * @Author: gchong3068
 * @Date: 2024/12/4 14:55
 * @Version: v1.0.0
 **/
@ConfigurationProperties(prefix = "elasticsearch")
@Component
@Data
public class ElasticSearchProperties {
    private String address;
    private String username;
    private String password;
}
