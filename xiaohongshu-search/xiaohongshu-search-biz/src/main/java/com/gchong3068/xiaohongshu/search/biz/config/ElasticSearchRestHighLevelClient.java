package com.gchong3068.xiaohongshu.search.biz.config;

import jakarta.annotation.Resource;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: elasticsearch rest high level client 配置
 * @Author: gchong3068
 * @Date: 2024/12/4 14:56
 * @Version: v1.0.0
 **/
@Configuration
public class ElasticSearchRestHighLevelClient {

    @Resource
    private ElasticSearchProperties elasticSearchProperties;


    private static final String COLON = ":";
    private static final String HTTP = "http";



    @Bean
    public RestHighLevelClient restHighLevelClient(){

        String address = elasticSearchProperties.getAddress();
        String username = elasticSearchProperties.getUsername();
        String password = elasticSearchProperties.getPassword();

        // 创建 CredentialsProvider 并设置身份验证凭据
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials(username,password));
        String[] addressArr = address.split(COLON);

        //ip地址
        String host = addressArr[0];
        //端口
        int port = Integer.parseInt(addressArr[1]);

        HttpHost httpHost = new HttpHost(host, port, HTTP);

        return new RestHighLevelClient(RestClient.builder(httpHost)
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                    httpAsyncClientBuilder.disableAuthCaching();
                    return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }));
    }
}
