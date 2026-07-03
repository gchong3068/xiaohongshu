package com.gchong3068.xiaohongshu.user.relation.biz;

import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.user.relation.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.user.relation.biz.enums.FollowUnfollowTypeEnum;
import com.gchong3068.xiaohongshu.user.relation.biz.model.dto.CountFollowUnfollowMqDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;


/**
 * @Description: 测试类
 * @Author: gchong3068
 * @Date: 2024/10/10 13:04
 * @Version: v1.0.0
 **/
@Slf4j
@SpringBootTest
class MQTests {

    @Resource
    private RocketMQTemplate rocketMQTemplate;


    /**
     * 发送MQ 计数
     */
    void testSendCountFollowUnfollowing(){
        for (long i = 0; i < 3200; i++) {
            CountFollowUnfollowMqDTO followUnfollowMqDTO = CountFollowUnfollowMqDTO.builder()
                    .userId(i+1)
                    .targetUserId(27L)
                    .type(FollowUnfollowTypeEnum.FOLLOW.getCode())
                    .build();

            Message<String> message = MessageBuilder.withPayload(JsonUtils.toJsonString(followUnfollowMqDTO)).build();

            rocketMQTemplate.asyncSend(MQConstants.TOPIC_COUNT_FANS, message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("==> 【计数服务：粉丝数】MQ 发送成功，SendResult: {}", sendResult);
                }

                @Override
                public void onException(Throwable throwable) {
                    log.error("==> 【计数服务：粉丝数】MQ 发送异常: ", throwable);
                }
            });

        }
    }
}
