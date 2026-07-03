package com.gchong3068.xiaohongshu.comment.biz.retry;

import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.comment.biz.model.dto.PublishCommentMqDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.retry.annotation.Retryable;

import java.time.LocalDateTime;


/**
 * @Description: 发送MQ重试
 * @Author: gchong3068
 * @Date: 2025/1/1 16:43
 * @Version: v1.0.0
 **/
@Component
@Slf4j
public class SendMqRetryHelper {

    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private RetryTemplate retryTemplate;
    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @Retryable(
            retryFor = {Exception.class},// 需要重试的异常类型
            maxAttempts = 3,        // 最大重试次数
            backoff = @Backoff(delay = 1000, multiplier = 2) // 初始延迟时间 1000ms，每次重试间隔加倍
    )
    public void send (String topic , PublishCommentMqDTO publishCommentMqDTO){
        log.info("==> 开始异步发送 MQ, Topic: {}, publishCommentMqDTO: {}", topic, publishCommentMqDTO);

        //构建信息对象，将DTO —> Json
        Message<String> message = MessageBuilder.withPayload(JsonUtils.toJsonString(publishCommentMqDTO)).build();

        //同步发送
        rocketMQTemplate.send(topic, message);
    }

    /**
     * 兜底方案: 将发送失败的 MQ 写入数据库，之后，通过定时任务扫表，将发送失败的 MQ 再次发送，最终发送成功后，将该记录物理删除
     */
    @Recover
    public void asyncSendMessageFallback(Exception e, String topic, PublishCommentMqDTO publishCommentMqDTO) {
        log.error("==> 多次发送失败, 进入兜底方案, Topic: {}, publishCommentMqDTO: {}", topic, publishCommentMqDTO);

        // TODO:
    }

    /**
     * 异步发送
     * @Auther: gchong3068
     * @Date: 2025/1/1 17:03
 	 * @param topic 信息标题
 	 * @param body 信息
     **/
    public void asyncSend(String topic ,String body){
        log.info("==> 开始异步发送 MQ, Topic: {}, Body: {}", topic, body);

        Message<String> message = MessageBuilder.withPayload(body).build();

        rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("==> 【评论发布】MQ 发送成功，SendResult: {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("==> 【评论发布】MQ 发送异常: ", throwable);
                handleRetry(topic, message);
            }
        });


    }


    /**
     * 重试处理
     * @Auther: gchong3068
     * @Date: 2025/1/1 17:05
 	 * @param topic 信息标题
 	 * @param message 信息
     **/
    private void handleRetry(String topic, Message<String> message) {
        //异步
        threadPoolTaskExecutor.submit(()->{
            try {
                // 通过 retryTemplate 执行重试
                retryTemplate.execute((RetryCallback<Void, RuntimeException>) context -> {
                    log.info("==> 开始重试 MQ 发送, 当前重试次数: {}, 时间: {}", context.getRetryCount() + 1, LocalDateTime.now());
                    // 同步发送 MQ
                    rocketMQTemplate.syncSend(topic, message);
                    return null;
                });
            } catch (Exception e) {
                // 多次重试失败，进入兜底方案
                fallback(e, topic, message.getPayload());
            }
        });
    }

    /**
     * 兜底方案: 将发送失败的 MQ 写入数据库，之后，通过定时任务扫表，将发送失败的 MQ 再次发送，最终发送成功后，将该记录物理删除
     */
    private void fallback(Exception e, String topic, String bodyJson) {
        log.error("==> 多次发送失败, 进入兜底方案, Topic: {}, bodyJson: {}", topic, bodyJson);

        // TODO:
    }

}
