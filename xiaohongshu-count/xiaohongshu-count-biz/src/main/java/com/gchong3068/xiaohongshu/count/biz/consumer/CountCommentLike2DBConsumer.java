package com.gchong3068.xiaohongshu.count.biz.consumer;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.nacos.shaded.com.google.common.util.concurrent.RateLimiter;
import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.count.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.count.biz.domain.mapper.CommentDOMapper;
import com.gchong3068.xiaohongshu.count.biz.model.dto.AggregationCountLikeUnLikeCommentMqDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: 计数: 评论点赞数落库
 * @Author: gchong3068
 * @Date: 2025/6/2 20:48
 * @Version: v1.0.0
 **/
@Component
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_" +  MQConstants.TOPIC_COUNT_COMMENT_LIKE_2_DB, // Group 组
        topic = MQConstants.TOPIC_COUNT_COMMENT_LIKE_2_DB // 主题 Topic
        )
@Slf4j
public class CountCommentLike2DBConsumer implements RocketMQListener<String> {

    @Resource
    private CommentDOMapper commentDOMapper;


    private RateLimiter rateLimiter = RateLimiter.create(5000);


    @Override
    public void onMessage(String body) {
        //流量削峰
        rateLimiter.acquire();

        log.info( "## 消费到了 MQ 【计数: 评论点赞数落库】, {}...", body);

        List<AggregationCountLikeUnLikeCommentMqDTO> countList = null;
        try{
            countList = JsonUtils.parseList(body, AggregationCountLikeUnLikeCommentMqDTO.class);
        }catch (Exception e){
            log.error("##解析Json 字符串异常",e);
        }

        if ( CollUtil.isNotEmpty(countList)){
            countList.forEach(item ->{
                Long commentID = item.getCommentId();
                Integer count = item.getCount();

                commentDOMapper.updateLikeTotalByCommentId(count,commentID);
            });
        }
    }
}
