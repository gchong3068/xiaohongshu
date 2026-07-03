package com.gchong3068.xiaohongshu.note.biz.consumer;

import com.alibaba.nacos.shaded.com.google.common.util.concurrent.RateLimiter;
import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.note.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.note.biz.domain.dataobject.NoteLikeDO;
import com.gchong3068.xiaohongshu.note.biz.domain.mapper.NoteLikeDOMapper;
import com.gchong3068.xiaohongshu.note.biz.model.dto.LikeUnlikeNoteMqDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @Description: 点赞点踩消息消费者
 * @Author: gchong3068
 * @Date: 2024/10/20 12:08
 * @Version: v1.0.0
 **/
@Component
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_" + MQConstants.TOPIC_LIKE_OR_UNLIKE,
        topic = MQConstants.TOPIC_LIKE_OR_UNLIKE,
        consumeMode = ConsumeMode.ORDERLY //顺序消费
)
@Slf4j
public class LikeUnlikeNoteConsumer implements RocketMQListener<Message> {

    private RateLimiter rateLimiter = RateLimiter.create(5000);
    @Resource
    private NoteLikeDOMapper noteLikeDOMapper;
    @Resource
    private RocketMQTemplate rocketMQTemplate;


    @Override
    public void onMessage(Message message) {
        rateLimiter.acquire();

        String bodyJsonStr = new String(message.getBody());
        //标签
        String tags = message.getTags();

        log.info("==> LikeUnlikeNoteConsumer 消费了消息 {}, tags: {}", bodyJsonStr, tags);

        if (Objects.equals(tags,MQConstants.TAG_LIKE)){
            handleLikeNoteTagMessage(bodyJsonStr);
        } else if (Objects.equals(tags, MQConstants.TAG_UNLIKE)) {
            handleUnlikeNoteTagMessage(bodyJsonStr);
        }
    }


    /**
     * 点赞
     * @Auther: gchong3068
     * @Date: 2024/10/20 12:15
 	 * @param bodyJsonStr 信息体
     **/
    private void handleLikeNoteTagMessage(String bodyJsonStr) {

        LikeUnlikeNoteMqDTO likeNoteMQDTO = JsonUtils.parseObject(bodyJsonStr, LikeUnlikeNoteMqDTO.class);

        if (Objects.isNull(likeNoteMQDTO)) return;

        // 用户ID
        Long userId = likeNoteMQDTO.getUserId();
        // 点赞的笔记ID
        Long noteId = likeNoteMQDTO.getNoteId();
        // 操作类型
        Integer type = likeNoteMQDTO.getType();
        // 点赞时间
        LocalDateTime createTime = likeNoteMQDTO.getCreateTime();

        NoteLikeDO noteLikeDO = NoteLikeDO.builder()
                .userId(userId)
                .noteId(noteId)
                .createTime(createTime)
                .status(type)
                .build();

        int count = noteLikeDOMapper.insertOrUpdate(noteLikeDO);

        if (count == 0) return;

        org.springframework.messaging.Message<String> message = MessageBuilder.withPayload(bodyJsonStr)
                .build();

        //异步发送MQ
        rocketMQTemplate.asyncSend(MQConstants.TOPIC_COUNT_NOTE_LIKE, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("==> 【计数: 笔记点赞】MQ 发送成功，SendResult: {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("==> 【计数: 笔记点赞】MQ 发送异常: ", throwable);
            }
        });

    }

    /**
     * 取消点赞
     * @Auther: gchong3068
     * @Date: 2024/10/20 12:15
 	 * @param bodyJsonStr 信息体
     **/
    private void handleUnlikeNoteTagMessage(String bodyJsonStr) {
        // 消息体 JSON 字符串转 DTO
        LikeUnlikeNoteMqDTO unlikeNoteMqDTO = JsonUtils.parseObject(bodyJsonStr, LikeUnlikeNoteMqDTO.class);

        if (Objects.isNull(unlikeNoteMqDTO)) return;

        // 用户ID
        Long userId = unlikeNoteMqDTO.getUserId();
        // 点赞的笔记ID
        Long noteId = unlikeNoteMqDTO.getNoteId();
        // 操作类型
        Integer type = unlikeNoteMqDTO.getType();
        // 点赞时间
        LocalDateTime createTime = unlikeNoteMqDTO.getCreateTime();

        // 构建 DO 对象
        NoteLikeDO noteLikeDO = NoteLikeDO.builder()
                .userId(userId)
                .noteId(noteId)
                .createTime(createTime)
                .status(type)
                .build();

        // 取消点赞：记录更新
        int count = noteLikeDOMapper.update2UnlikeByUserIdAndNoteId(noteLikeDO);

        if (count == 0) return;
        // 更新数据库成功后，发送计数 MQ
        org.springframework.messaging.Message<String> message = MessageBuilder.withPayload(bodyJsonStr).build();

        //异步发送MQ
        rocketMQTemplate.asyncSend(MQConstants.TOPIC_COUNT_NOTE_LIKE, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("==> 【计数: 笔记取消点赞】MQ 发送成功，SendResult: {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("==> 【计数: 笔记取消点赞】MQ 发送异常: ", throwable);
            }
        });
    }

}
