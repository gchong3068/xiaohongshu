package com.gchong3068.xiaohongshu.count.biz.consumer;

import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.count.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.count.biz.constant.RedisKeyConstants;
import com.gchong3068.xiaohongshu.count.biz.domain.mapper.UserCountDOMapper;
import com.gchong3068.xiaohongshu.count.biz.model.dto.NoteOperateMqDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Description: 计数: 笔记发布数
 * @Author: gchong3068
 * @Date: 2024/11/2 17:37
 * @Version: v1.0.0
 **/
@Component
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_" + MQConstants.TOPIC_NOTE_OPERATE,
        topic = MQConstants.TOPIC_NOTE_OPERATE)
@Slf4j
public class CountNotePublishConsumer implements RocketMQListener<Message> {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private UserCountDOMapper userCountDOMapper;


    @Override
    public void onMessage(Message message) {

        //信息体
        String bodyJsonStr = new String(message.getBody());
        //标签
        String tags = message.getTags();

        log.info("==> CountNotePublishConsumer 消费了消息 {}, tags: {}", bodyJsonStr, tags);

        if (Objects.equals(tags,MQConstants.TAG_NOTE_PUBLISH)){
            handleTagMessage(bodyJsonStr,1);
        } else if (Objects.equals(tags, MQConstants.TAG_NOTE_DELETE)) {
            handleTagMessage(bodyJsonStr,-1);
        }
    }

    /**
     * 笔记发布,删除
     * @Auther: gchong3068
     * @Date: 2024/11/2 17:41
     * @param bodyJsonStr 消息体
     **/
    public void handleTagMessage(String bodyJsonStr ,long count){
        NoteOperateMqDTO noteOperateMqDTO = JsonUtils.parseObject(bodyJsonStr, NoteOperateMqDTO.class);

        if (Objects.isNull(noteOperateMqDTO)) return;

        Long creatorId = noteOperateMqDTO.getCreatorId();

        // 更新 Redis 中用户维度的计数 Hash
        String countUserRedisKey = RedisKeyConstants.buildCountUserKey(creatorId);
        // 判断 Redis 中 Hash 是否存在
        boolean isCountUserExisted = redisTemplate.hasKey(countUserRedisKey);

        // 若存在才会更新
        // (因为缓存设有过期时间，考虑到过期后，缓存会被删除，这里需要判断一下，存在才会去更新，而初始化工作放在查询计数来做)
        if (isCountUserExisted) {
            // 对目标用户 Hash 中的笔记发布总数，进行加减操作
            redisTemplate.opsForHash().increment(countUserRedisKey, RedisKeyConstants.FIELD_NOTE_TOTAL, count);
        }

        // 更新 t_user_count 表
        userCountDOMapper.insertOrUpdateNoteTotalByUserId(count, creatorId);

    }


}
