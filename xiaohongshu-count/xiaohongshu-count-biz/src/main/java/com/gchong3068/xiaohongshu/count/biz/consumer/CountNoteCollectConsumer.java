package com.gchong3068.xiaohongshu.count.biz.consumer;

import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.count.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.count.biz.constant.RedisKeyConstants;
import com.gchong3068.xiaohongshu.count.biz.enums.CollectUnCollectNoteTypeEnum;
import com.gchong3068.xiaohongshu.count.biz.model.dto.CountCollectUnCollectNoteMqDTO;
import com.github.phantomthief.collection.BufferTrigger;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 计数: 笔记收藏数
 * @Author: gchong3068
 * @Date: 2024/10/31 15:13
 * @Version: v1.0.0
 **/
@Component
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_" + MQConstants.TOPIC_COUNT_NOTE_COLLECT,
        topic = MQConstants.TOPIC_COUNT_NOTE_COLLECT)
@Slf4j
public class CountNoteCollectConsumer implements RocketMQListener<String> {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    private BufferTrigger<String> bufferTrigger = BufferTrigger.<String> batchBlocking()
            .bufferSize(50000)
            .batchSize(1000)
            .linger(Duration.ofSeconds(1))
            .setConsumerEx(this::consumerMessage)
            .build();


    @Override
    public void onMessage(String item) {
        bufferTrigger.enqueue(item);
    }


    private void consumerMessage(List<String> bodys) {
        log.info("==> 【笔记收藏数】聚合消息, size: {}", bodys.size());
        log.info("==> 【笔记收藏数】聚合消息, {}", JsonUtils.toJsonString(bodys));

        // List<String> 转 List<CountLikeUnlikeNoteMqDTO>
        List<CountCollectUnCollectNoteMqDTO> countCollectUnCollectNoteMqDTOS = bodys.stream()
                .map(body -> JsonUtils.parseObject(body, CountCollectUnCollectNoteMqDTO.class)).toList();

        //按笔记Id 进行分类
        Map<Long,List<CountCollectUnCollectNoteMqDTO>> groupMap = countCollectUnCollectNoteMqDTOS.stream()
                .collect(Collectors.groupingBy(CountCollectUnCollectNoteMqDTO::getNoteId));

        //按组汇总
        Map<Long, Integer> countMap = Maps.newHashMap();

        for (Map.Entry<Long,List<CountCollectUnCollectNoteMqDTO>> entry :groupMap.entrySet()){
            List<CountCollectUnCollectNoteMqDTO> list = entry.getValue();

            int finalCount = 0;

            for (CountCollectUnCollectNoteMqDTO countCollectUnCollectNoteMqDTO :list){
                Integer type = countCollectUnCollectNoteMqDTO.getType();

                CollectUnCollectNoteTypeEnum collectUnCollectNoteTypeEnum = CollectUnCollectNoteTypeEnum.valueOf(type);

                if (Objects.isNull(collectUnCollectNoteTypeEnum)) continue;

                switch(collectUnCollectNoteTypeEnum){
                    case COLLECT -> finalCount += 1;
                    case UN_COLLECT -> finalCount -= 1;
                }
            }
            //将分组后统计出的最终计数，存入 countMap 中
            countMap.put(entry.getKey(), finalCount);
        }
        log.info("## 【笔记收藏数】聚合后的计数数据: {}", JsonUtils.toJsonString(countMap));

        //更新Redis
        countMap.forEach((k,v)->{
            String redisKey = RedisKeyConstants.buildCountUserKey(k);
            // 判断 Redis 中 Hash 是否存在
            boolean isExisted = redisTemplate.hasKey(redisKey);

            // 若存在才会更新
            // (因为缓存设有过期时间，考虑到过期后，缓存会被删除，这里需要判断一下，存在才会去更新，而初始化工作放在查询计数来做)
            if (isExisted) {
                // 对目标用户 Hash 中的收藏总数字段进行计数操作
                redisTemplate.opsForHash().increment(redisKey, RedisKeyConstants.FIELD_COLLECT_TOTAL, v);
            }
        });

        //发送MQ 数据库落库
        Message<String> message = MessageBuilder.withPayload(JsonUtils.toJsonString(countMap)).build();

        rocketMQTemplate.asyncSend(MQConstants.TOPIC_COUNT_NOTE_COLLECT_2_DB, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("==> 【计数服务：笔记收藏数入库】MQ 发送成功，SendResult: {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("==> 【计数服务：笔记收藏数入库】MQ 发送异常: ", throwable);
            }
        });
    }
}
