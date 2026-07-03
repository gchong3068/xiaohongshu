package com.gchong3068.xiaohongshu.note.biz.consumer;

import com.gchong3068.xiaohongshu.note.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.note.biz.service.NoteService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @Description: RocketMQ消费者
 * @Author: gchong3068
 * @Date: 2024/8/31 18:50
 * @Version: v1.0.0
 **/
@Component
@Slf4j
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_" + MQConstants.TOPIC_DELETE_NOTE_LOCAL_CACHE,  //group 名称
        topic = MQConstants.TOPIC_DELETE_NOTE_LOCAL_CACHE,  //主题Topic
        messageModel = MessageModel.BROADCASTING) //广播模式
public class DeleteNoteLocalCacheConsumer implements RocketMQListener<String> {

    @Resource
    private NoteService noteService;

    @Override
    public void onMessage(String body) {
        Long noteId = Long.valueOf(body);
        log.info("## 消费者消费成功， noteId: {}",noteId);

        noteService.deleteNoteLocalCache(noteId);
    }
}
