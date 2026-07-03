package com.gchong3068.xiaohongshu.comment.biz.consumer;

import com.gchong3068.xiaohongshu.comment.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.comment.biz.service.CommentService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @Description: 删除本地评论缓存
 * @Author: gchong3068
 * @Date: 2025/6/2 21:44
 * @Version: v1.0.0
 **/
@Component
@Slf4j
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_" + MQConstants.TOPIC_DELETE_COMMENT_LOCAL_CACHE, // Group
        topic = MQConstants.TOPIC_DELETE_COMMENT_LOCAL_CACHE, // 消费的主题 Topic
        messageModel = MessageModel.BROADCASTING) // 广播模式
public class DeleteCommentLocalCacheConsumer implements RocketMQListener<String> {

    @Resource
    private CommentService commentService;


    @Override
    public void onMessage(String body) {
        Long commentId = Long.valueOf(body);
        log.info( "## 消费者消费成功， commentId: {}",commentId);
        commentService.deleteCommentLocalCache(commentId);
    }
}
