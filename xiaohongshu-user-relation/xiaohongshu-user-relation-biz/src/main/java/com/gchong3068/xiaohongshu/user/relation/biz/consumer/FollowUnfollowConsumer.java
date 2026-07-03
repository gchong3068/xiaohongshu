package com.gchong3068.xiaohongshu.user.relation.biz.consumer;

import com.alibaba.nacos.shaded.com.google.common.util.concurrent.RateLimiter;
import com.gchong3068.framework.common.util.DateUtils;
import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.user.relation.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.user.relation.biz.constant.RedisKeyConstants;
import com.gchong3068.xiaohongshu.user.relation.biz.domain.dataobject.FansDO;
import com.gchong3068.xiaohongshu.user.relation.biz.domain.dataobject.FollowingDO;
import com.gchong3068.xiaohongshu.user.relation.biz.domain.mapper.FansDOMapper;
import com.gchong3068.xiaohongshu.user.relation.biz.domain.mapper.FollowingDOMapper;
import com.gchong3068.xiaohongshu.user.relation.biz.enums.FollowUnfollowTypeEnum;
import com.gchong3068.xiaohongshu.user.relation.biz.model.dto.CountFollowUnfollowMqDTO;
import com.gchong3068.xiaohongshu.user.relation.biz.model.dto.FollowUserMqDTO;
import com.gchong3068.xiaohongshu.user.relation.biz.model.dto.UnfollowUserMqDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

/**
 * @Description: 关注/取关 消费者
 * @Author: gchong3068
 * @Date: 2024/9/15 10:20
 * @Version: v1.0.0
 **/
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_" + MQConstants.TOPIC_FOLLOW_OR_UNFOLLOW,
        topic = MQConstants.TOPIC_FOLLOW_OR_UNFOLLOW,
        consumeMode = ConsumeMode.ORDERLY)
public class FollowUnfollowConsumer implements RocketMQListener<Message> {

    @Resource
    private FollowingDOMapper followingDOMapper;
    @Resource
    private FansDOMapper fansDOMapper;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private RateLimiter rateLimiter;
    @Resource
    private RocketMQTemplate rocketMQTemplate;


    @Override
    public void onMessage(Message message) {
        //流量削峰
        rateLimiter.acquire();
        //信息体
        String bodyJsonStr = new String(message.getBody());
        String tags = message.getTags();

        log.info("==> FollowUnfollowConsumer 消费了消息 {}, tags: {}", bodyJsonStr, tags);

        if (Objects.equals(tags,MQConstants.TAG_FOLLOW)){ //关注
            handleFollowTagMessage(bodyJsonStr);
        } else if (Objects.equals(tags,MQConstants.TAG_UNFOLLOW)) { //取关
            handleUnfollowTagMessage(bodyJsonStr);
        }
    }

    /**
     * 关注
     * @Auther: gchong3068
     * @Date: 2024/9/15 10:43
     * @param bodyJsonStr 关注消息体
     **/
    private void handleFollowTagMessage(String bodyJsonStr){
        FollowUserMqDTO followUserMqDTO = JsonUtils.parseObject(bodyJsonStr, FollowUserMqDTO.class);

        if (Objects.isNull(followUserMqDTO)) return;

        //幂等性
        Long userId = followUserMqDTO.getUserid();
        Long followUserId = followUserMqDTO.getFollowUserId();
        LocalDateTime createTime = followUserMqDTO.getCreateTime();

        boolean isSuccess = Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            try{
                //关注表
                int count = followingDOMapper.insert(FollowingDO.builder()
                                .userId(userId)
                                .followingUserId(followUserId)
                                .createTime(createTime)
                                .build());
                //粉丝表
                if (count > 0){
                    fansDOMapper.insert(FansDO.builder()
                            .userId(followUserId)
                            .fansUserId(userId)
                            .createTime(createTime)
                            .build());
                }
                return  true;
            }catch (Exception ex){
                status.setRollbackOnly();
                log.error("",ex);
            }
            return false;
        }));

        log.info("## 数据库添加记录结果：{}",isSuccess);
        //更新Redis中被关注用户ZSet列表
        if (isSuccess){
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptSource(new ResourceScriptSource(new ClassPathResource("/lua/follow_check_and_update_fans_zset.lua")));
            script.setResultType(Long.class);

            //时间戳
            long timestamp = DateUtils.localDateTime2Timestamp(createTime);

            String fansRedisKey = RedisKeyConstants.buildUserFansKey(followUserId);
            //执行脚本
            redisTemplate.execute(script, Collections.singletonList(fansRedisKey),userId,timestamp);

            //MQ通知计数服务
            CountFollowUnfollowMqDTO countFollowUnfollowMqDTO = CountFollowUnfollowMqDTO.builder()
                    .userId(userId)
                    .targetUserId(followUserId)
                    .type(FollowUnfollowTypeEnum.FOLLOW.getCode()) //关注
                    .build();
            //发送MQ
            sendMQ(countFollowUnfollowMqDTO);
        }
    }

    /**
     * 取关
     * @Auther: gchong3068
     * @Date: 2024/9/23 11:01 
     * @param bodyJsonStr 取关信息体
     **/
    private void handleUnfollowTagMessage(String bodyJsonStr){
        UnfollowUserMqDTO unfollowUserMqDTO = JsonUtils.parseObject(bodyJsonStr, UnfollowUserMqDTO.class);

        if (Objects.isNull(unfollowUserMqDTO)) return;

        Long userId = unfollowUserMqDTO.getUserId();
        Long unfollowUserId = unfollowUserMqDTO.getUnfollowUserId();
        LocalDateTime createTime = unfollowUserMqDTO.getCreateTime();

        //编程式取关事务
        boolean isSuccess = Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            try{
                int count = followingDOMapper.deleteByUserIdAndFollowingUserId(userId, unfollowUserId);
                if (count>0){
                    fansDOMapper.deleteByUserIdAndFansUserId(unfollowUserId,userId);
                }
                return true;
            }catch (Exception e){
                status.setRollbackOnly();
                log.error("",e);
            }
            return false;
        }));
        log.info("## 数据库删除记录结果：{}",isSuccess);

        if (isSuccess){
            String fansRedisKey = RedisKeyConstants.buildUserFansKey(unfollowUserId);
            redisTemplate.opsForZSet().remove(fansRedisKey,userId);

            //MQ计数服务
            CountFollowUnfollowMqDTO countFollowUnfollowMqDTO = CountFollowUnfollowMqDTO.builder()
                    .userId(userId)
                    .targetUserId(unfollowUserId)
                    .type(FollowUnfollowTypeEnum.UNFOLLOW.getCode())  //取关
                    .build();

            sendMQ(countFollowUnfollowMqDTO);
        }
    }



    /**
     * 发送MQ
     * @Auther: gchong3068
     * @Date: 2024/11/2 18:05
     * @param countFollowUnfollowMqDTO 消息体
     **/
    private void sendMQ(CountFollowUnfollowMqDTO countFollowUnfollowMqDTO){
        org.springframework.messaging.Message<String> message = MessageBuilder.withPayload(JsonUtils.toJsonString(countFollowUnfollowMqDTO)).build();

        //异步发送MQ
        rocketMQTemplate.asyncSend(MQConstants.TOPIC_COUNT_FOLLOWING, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("==> 【计数服务：关注数】MQ 发送成功，SendResult: {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("==> 【计数服务：关注数】MQ 发送异常: ", throwable);
            }
        });

        //发送MQ计数服务
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
