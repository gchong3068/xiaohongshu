package com.gchong3068.xiaohongshu.count.biz.consumer;


import cn.hutool.core.collection.CollUtil;
import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.count.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.count.biz.domain.mapper.UserCountDOMapper;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description: 计数粉丝数 存库
 * @Author: gchong3068
 * @Date: 2024/10/11 18:24
 * @Version: v1.0.0
 **/
@Component
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_" + MQConstants.TOPIC_COUNT_FANS_2_DB,
    topic = MQConstants.TOPIC_COUNT_FANS_2_DB
)
@Slf4j
public class CountFans2DBConsumer implements RocketMQListener<String> {

    private final UserCountDOMapper userCountDOMapper;
    //Guava令牌 每秒5000个
    private RateLimiter rateLimiter = RateLimiter.create(5000);

    public CountFans2DBConsumer(UserCountDOMapper userCountDOMapper) {
        this.userCountDOMapper = userCountDOMapper;
    }
    @Override
    public void onMessage(String body) {
        //流量削峰
        rateLimiter.acquire();
        log.info("## 消费到了 MQ 【计数: 粉丝数入库】, {}...", body);

        Map<Long,Integer> countMap = null;

        try{
            countMap = JsonUtils.parseMap(body,Long.class,Integer.class);
        }catch (Exception e){
            log.error("## 解析Json字符串异常: ",e);
        }

        if (CollUtil.isNotEmpty(countMap)){
            countMap.forEach((k,v)-> userCountDOMapper.insertOrUpdateFansTotalByUserId(v,k));
        }
    }
}
