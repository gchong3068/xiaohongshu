package com.gchong3068.xiaohongshu.comment.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 查询评论分页数据
 * @Author: gchong3068
 * @Date: 2025/4/1 16:11
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCommentItemRspVO {
    /**
     * 评论 ID
     */
    private Long commentId;

    /**
     * 发布者用户 ID
     */
    private Long userId;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论内容
     */
    private String imageUrl;

    /**
     * 发布时间
     */
    private String createTime;

    /**
     * 被点赞数
     */
    private Long likeTotal;

    /**
     * 二级评论总数
     */
    private Long childCommentTotal;

    /**
     * 最早回复的评论
     */
    private FindCommentItemRspVO firstReplyComment;

    /**
     * 热度值
     */
    private Double heat;

}
