package com.gchong3068.xiaohongshu.count.biz.consumer;

import cn.hutool.core.collection.CollUtil;
import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.count.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.count.biz.domain.mapper.NoteCountDOMapper;
import com.gchong3068.xiaohongshu.count.biz.domain.mapper.UserCountDOMapper;
import com.gchong3068.xiaohongshu.count.biz.model.dto.AggregationCountLikeUnlikeNoteMqDTO;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * @Description: 计数: 笔记点赞数落库
 * @Author: gchong3068
 * @Date: 2024/10/27 21:13
 * @Version: v1.0.0
 **/
@Component
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_" + MQConstants.TOPIC_COUNT_NOTE_LIKE_2_DB,
        topic = MQConstants.TOPIC_COUNT_NOTE_LIKE_2_DB)
@Slf4j
public class CountNoteLike2DBConsumer implements RocketMQListener<String> {

    @Resource
    private NoteCountDOMapper noteCountDOMapper;
    @Resource
    private UserCountDOMapper userCountDOMapper;
    @Resource
    private TransactionTemplate transactionTemplate;

    private RateLimiter rateLimiter = RateLimiter.create(5000);

    @Override
    public void onMessage(String body) {
        // 流量削峰：通过获取令牌，如果没有令牌可用，将阻塞，直到获得
        rateLimiter.acquire();

        log.info("## 消费到了 MQ 【计数: 笔记点赞数入库】, {}...", body);

        List<AggregationCountLikeUnlikeNoteMqDTO> countList = null;
        try{
            countList = JsonUtils.parseList(body,AggregationCountLikeUnlikeNoteMqDTO.class);
        }catch (Exception e){
            log.error("## 解析 JSON 字符串异常");
        }

        if (CollUtil.isNotEmpty(countList)){
            //判断数据库中t_user_count and t_note_count表，是否存在该笔记的点赞数，不存在则插入，存在则更新
            countList.forEach(item->{
                Long creatorId = item.getCreatorId();
                Long noteId = item.getNoteId();
                Integer count = item.getCount();

                transactionTemplate.execute(status -> {
                    try{
                        noteCountDOMapper.insertOrUpdateLikeTotalByNoteId(count,noteId);
                        userCountDOMapper.insertOrUpdateLikeTotalByUserId(count,creatorId);
                        return true;
                    }catch (Exception e){
                        status.setRollbackOnly();
                        log.error("",e);
                    }
                    return false;
                });
            });
        }
    }
}
