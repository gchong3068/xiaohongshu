package com.gchong3068.xiaohongshu.comment.biz.constant;

/**
 * @Description: MQ 常量
 * @Author: gchong3068
 * @Date: 2024/12/23 20:30
 * @Version: v1.0.0
 **/
public interface MQConstants {

    /**
     * Topic: 评论发布
     */
    String TOPIC_PUBLISH_COMMENT = "PublishCommentTopic";

    /**
     * Topic: 笔记评论总数
     */
    String TOPIC_COUNT_NOTE_COMMENT = "CountNoteCommentTopic";

    /**
     * Topic: 评论热度值更新
     */
    String TOPIC_COMMENT_HEAT_UPDATE = "CommentHeatUpdateTopic";


    /**
     * Topic: 评论点赞、取消点赞共用一个 Topic
     */
    String TOPIC_COMMENT_LIKE_OR_UNLIKE = "CommentLikeUnlikeTopic";

    /**
     * Topic: 删除本地缓存 —— 评论详情
     */
    String TOPIC_DELETE_COMMENT_LOCAL_CACHE = "DeleteCommentDetailLocalCacheTopic";

    /**
     * Topic: 删除评论
     */
    String TOPIC_DELETE_COMMENT = "DeleteCommentTopic";

    /**
     * Tag 标签：点赞
     */
    String TAG_LIKE = "Like";

    /**
     * Tag 标签：取消点赞
     */
    String TAG_UNLIKE = "UnLike";


}
