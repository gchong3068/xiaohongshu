package com.gchong3068.xiaohongshu.comment.biz.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 评论热度
 * @Author: gchong3068
 * @Date: 2025/2/7 20:31
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentHeatBO {
    /**
     * 评论 ID
     */
    private Long id;

    /**
     * 热度值
     */
    private Double heat;

    /**
     * 笔记 ID
     */
    private Long noteId;
}
