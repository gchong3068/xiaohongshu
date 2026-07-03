package com.gchong3068.xiaohongshu.count.biz.consumer;

import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.count.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.count.biz.constant.RedisKeyConstants;
import com.gchong3068.xiaohongshu.count.biz.enums.LikeUnlikeNoteTypeEnum;
import com.gchong3068.xiaohongshu.count.biz.model.dto.AggregationCountLikeUnlikeNoteMqDTO;
import com.gchong3068.xiaohongshu.count.biz.model.dto.CountLikeUnlikeNoteMqDTO;
import com.github.phantomthief.collection.BufferTrigger;
import com.google.common.collect.Lists;
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
 * @Description: 计数：笔记点赞数
 * @Author: gchong3068
 * @Date: 2024/10/27 20:37
 * @Version: v1.0.0
 **/
@Component
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_" + MQConstants.TOPIC_COUNT_NOTE_LIKE,
        topic = MQConstants.TOPIC_COUNT_NOTE_LIKE)
@Slf4j
public class CountNoteLikeConsumer implements RocketMQListener<String> {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    private BufferTrigger<String> bufferTrigger = BufferTrigger.<String>batchBlocking()
            .bufferSize(50000)
            .batchSize(1000)
            .linger(Duration.ofSeconds(1))
            .setConsumerEx(this::consumeMessage)
            .build();


    @Override
    public void onMessage(String body) {
        bufferTrigger.enqueue(body);
    }

    private void consumeMessage(List<String> bodys) {
        log.info("==> 【笔记点赞数】聚合消息, size: {}", bodys.size());
        log.info("==> 【笔记点赞数】聚合消息, {}", JsonUtils.toJsonString(bodys));

        // List<String> 转 List<CountLikeUnlikeNoteMqDTO>
        List<CountLikeUnlikeNoteMqDTO> countLikeUnlikeNoteMqDTOS = bodys.stream()
                .map(body -> JsonUtils.parseObject(body, CountLikeUnlikeNoteMqDTO.class)).toList();

        //按笔记Id 进行分类
        Map<Long,List<CountLikeUnlikeNoteMqDTO>> groupMap = countLikeUnlikeNoteMqDTOS.stream()
                .collect(Collectors.groupingBy(CountLikeUnlikeNoteMqDTO::getNoteId));


        //按组汇总数据
        //key 为笔记 ID, value 为最终操作的计数
        List<AggregationCountLikeUnlikeNoteMqDTO> countList = Lists.newArrayList();

        Map<Long, Integer> countMap = Maps.newHashMap();

        for (Map.Entry<Long,List<CountLikeUnlikeNoteMqDTO>> entry :groupMap.entrySet()){
            Long noteId = entry.getKey();
            List<CountLikeUnlikeNoteMqDTO> list = entry.getValue();

            Long creatorId = null;
            int finalCount = 0;
            for (CountLikeUnlikeNoteMqDTO   countLikeUnlikeNoteMqDTO  : list) {
                //操作类型
                Integer type = countLikeUnlikeNoteMqDTO.getType();

                LikeUnlikeNoteTypeEnum likeUnlikeNoteTypeEnum = LikeUnlikeNoteTypeEnum.valueOf(type);

                if (Objects.isNull(likeUnlikeNoteTypeEnum)) continue;

                switch(likeUnlikeNoteTypeEnum){
                    case LIKE -> finalCount += 1;
                    case UNLIKE -> finalCount -= 1;
                }

            }
            // 将分组后统计出的最终计数，存入 countMap 中
            countList.add(AggregationCountLikeUnlikeNoteMqDTO.builder()
                            .noteId(noteId)
                            .creatorId(creatorId)
                            .count(finalCount)
                            .build());

        }
        log.info("## 【笔记点赞数】聚合后的计数数据: {}", JsonUtils.toJsonString(countMap));

        //更新Redis
        countList.forEach(item ->{
            Long creatorId = item.getCreatorId();
            Long noteId = item.getNoteId();
            Integer count = item.getCount();

            String countNoteRedisKey = RedisKeyConstants.buildCountNoteKey(noteId);
            // 判断 Redis 中 Hash 是否存在
            Boolean isCountNoteExisted = redisTemplate.hasKey(countNoteRedisKey);

            if (isCountNoteExisted){
                redisTemplate.opsForHash().increment(countNoteRedisKey,RedisKeyConstants.FIELD_LIKE_TOTAL,count);
            }

            //更新Redis用户纬度点赞
            String countUserRedisKey = RedisKeyConstants.buildCountUserKey(creatorId);
            Boolean isCountUserExisted = redisTemplate.hasKey(countUserRedisKey);
            if (isCountUserExisted){
                redisTemplate.opsForHash().increment(countUserRedisKey,RedisKeyConstants.FIELD_LIKE_TOTAL,count);
            }


        });

        //发送MQ ，点赞落库
        Message<String> message = MessageBuilder.withPayload(JsonUtils.toJsonString(countMap)).build();

        rocketMQTemplate.asyncSend(MQConstants.TOPIC_COUNT_NOTE_LIKE_2_DB, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("==> 【计数服务：笔记点赞数入库】MQ 发送成功，SendResult: {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("==> 【计数服务：笔记点赞数入库】MQ 发送异常: ", throwable);
            }
        });
    }
}
