package com.gchong3068.xiaohongshu.comment.biz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 计数服务：评论发布
 * @Author: gchong3068
 * @Date: 2025/1/10 17:16
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CountPublishCommentMqDTO {
    /**
     * 笔记 ID
     */
    private Long noteId;

    /**
     * 评论 ID
     */
    private Long commentId;

    /**
     * 评论级别
     */
    private Integer level;

    /**
     * 父 ID
     */
    private Long parentId;
}
