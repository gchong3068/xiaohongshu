package com.gchong3068.xiaohongshu.data.align.constant;

/**
 * @Description: MQ 常量
 * @Author: gchong3068
 * @Date: 2024/11/9 18:24
 * @Version: v1.0.0
 **/
public interface MQConstants {

    /**
     * Topic: 计数 - 笔记点赞数
     */
    String TOPIC_COUNT_NOTE_LIKE = "CountNoteLikeTopic";
    /**
     * Topic: 计数 - 笔记收藏数
     */
    String TOPIC_COUNT_NOTE_COLLECT = "CountNoteCollectTopic";

    /**
     * Topic: 笔记操作(发布删除)
     **/
    String TOPIC_NOTE_OPERATE = "NoteOperateTopic";

    /**
     * Topic: 关注数计数
     */
    String TOPIC_COUNT_FOLLOWING = "CountFollowingTopic";

}
