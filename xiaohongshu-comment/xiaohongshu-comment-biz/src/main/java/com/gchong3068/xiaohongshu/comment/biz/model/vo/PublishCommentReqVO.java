package com.gchong3068.xiaohongshu.comment.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 评论发布
 * @Author: gchong3068
 * @Date: 2024/12/23 20:13
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublishCommentReqVO {

    @NotNull(message = "笔记ID 不能为空")
    private Long noteId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论图片链接
     */
    private String imageUrl;

    /**
     * 回复的哪个评论（评论 ID）
     */
    private Long replyCommentId;
}
