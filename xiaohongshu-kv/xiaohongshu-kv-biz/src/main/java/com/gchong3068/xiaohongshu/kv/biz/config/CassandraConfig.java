package com.gchong3068.xiaohongshu.kv.biz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;

/**
 * @Description: Cassandra 配置
 * @Author: gchong3068
 * @Date: 2024/7/26 23:11
 * @Version: v1.0.0
 **/
@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {


    @Value("${spring.cassandra.keyspace-name}")
    private String keySpace;

    @Value("${spring.cassandra.contact-points}")
    private String contactPoints;

    @Value("${spring.cassandra.port}")
    private int port;


    @Override
    protected String getKeyspaceName() {
        return keySpace;
    }

    @Override
    protected String getContactPoints() {
        return contactPoints;
    }

    @Override
    protected int getPort() {
        return port;
    }
}
