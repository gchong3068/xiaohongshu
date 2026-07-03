package com.gchong3068.xiaohongshu.search.biz.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * @Description: Canal 客户端
 * @Author: gchong3068
 * @Date: 2024/12/16 11:07
 * @Version: v1.0.0
 **/
@Component
@Slf4j
public class CanalClient implements DisposableBean {

    @Resource
    private CanalProperties canalProperties;

    private CanalConnector canalConnector;



    /**
     * 实例化 Canal 链接对象
     * @Auther: gchong3068
     * @Date: 2024/12/16 11:17 
     * @return com.alibaba.otter.canal.client.CanalConnector
     **/
    @Bean
    public CanalConnector getCanalConnector() {
        String address = canalProperties.getAddress();

        String[] addressArr = address.split(":");

        String host = addressArr[0];
        int port = Integer.parseInt(addressArr[1]);

        canalConnector = CanalConnectors.newSingleConnector(
                    new InetSocketAddress(host,port),
                canalProperties.getDestination(),
                canalProperties.getUsername(),
                canalProperties.getPassword());

        //连接到Canal
        canalConnector.connect();
        // 订阅 Canal 中的数据变化，指定要监听的数据库和表（可以使用表名、数据库名的通配符）
        canalConnector.subscribe(canalProperties.getSubscribe());
        // 回滚 Canal 消费者的位点，回滚到上次提交的消费位置
        canalConnector.rollback();
        return canalConnector;

    }



    /**
     * 在 Spring 容器销毁时释放
     * @Auther: gchong3068
     * @Date: 2024/12/16 11:18 
     **/
    @Override
    public void destroy() throws Exception {
        if (Objects.nonNull(canalConnector)){
            canalConnector.disconnect();
        }
    }
}
