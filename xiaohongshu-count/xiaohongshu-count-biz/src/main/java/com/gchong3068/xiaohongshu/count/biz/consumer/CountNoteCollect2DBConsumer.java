package com.gchong3068.xiaohongshu.count.biz.consumer;

import cn.hutool.core.collection.CollUtil;
import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.count.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.count.biz.domain.mapper.NoteCountDOMapper;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description: 计数: 笔记收藏数落库
 * @Author: gchong3068
 * @Date: 2024/10/31 15:47
 * @Version: v1.0.0
 **/
@Component
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_" + MQConstants.TOPIC_COUNT_NOTE_COLLECT_2_DB,
        topic = MQConstants.TOPIC_COUNT_NOTE_COLLECT_2_DB)
@Slf4j
public class CountNoteCollect2DBConsumer implements RocketMQListener<String> {

    @Resource
    private NoteCountDOMapper noteCountDOMapper;

    private RateLimiter rateLimiter = RateLimiter.create(5000);



    @Override
    public void onMessage(String body) {
        rateLimiter.acquire();
        log.info("## 消费到了 MQ 【计数: 笔记收藏数入库】, {}...", body);
        Map<Long,Integer>  countMap = null;

        try{
            countMap = JsonUtils.parseMap(body,Long.class,Integer.class);
        }catch (Exception e){
            log.error("##解析JSON 字符串异常");
        }

        if (CollUtil.isNotEmpty(countMap)){
            countMap.forEach((k,v)-> noteCountDOMapper.insertOrUpdateCollectTotalByNoteId(v,k));
        }
    }
}
