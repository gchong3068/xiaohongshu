package com.gchong3068.xiaohongshu.count.biz.consumer;

import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.count.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.count.biz.enums.FollowUnfollowTypeEnum;
import com.gchong3068.xiaohongshu.count.biz.domain.mapper.UserCountDOMapper;
import com.gchong3068.xiaohongshu.count.biz.model.dto.CountFollowUnfollowMqDTO;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Description: 计数：关注数存库
 * @Author: gchong3068
 * @Date: 2024/10/13 23:14
 * @Version: v1.0.0
 **/
@Component
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_" + MQConstants.TOPIC_COUNT_FOLLOWING_2_DB,
    topic = MQConstants.TOPIC_COUNT_FOLLOWING_2_DB)
@Slf4j
public class CountFollowing2DBConsumer implements RocketMQListener<String> {

    @Resource
    private UserCountDOMapper userCountDOMapper;
    //每秒创建5000令牌
    private RateLimiter rateLimiter = RateLimiter.create(5000);


    @Override
    public void onMessage(String item) {
        rateLimiter.acquire();
        log.info("## 消费到了 MQ 【计数: 关注数入库】, {}...", item);
        if (StringUtils.isBlank(item)) return;

        CountFollowUnfollowMqDTO countFollowUnfollowMqDTO = JsonUtils.parseObject(item, CountFollowUnfollowMqDTO.class);
        //操作类型
        Integer type = countFollowUnfollowMqDTO.getType();
        Long userId = countFollowUnfollowMqDTO.getUserId();

        int count = Objects.equals(type, FollowUnfollowTypeEnum.FOLLOW.getCode()) ? 1 : -1;
        //更新关注数
        userCountDOMapper.insertOrUpdateFollowingTotalByUserId(count,userId);
    }
}
