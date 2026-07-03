package com.gchong3068.xiaohongshu.count.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 查询笔记维度相关计数
 * @Author: gchong3068
 * @Date: 2025/6/10 18:58
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindNoteCountsByIdRspDTO {

    /**
     * 笔记 ID
     */
    private Long noteId;

    /**
     * 点赞数
     */
    private Long likeTotal;

    /**
     * 收藏数
     */
    private Long collectTotal;

    /**
     * 评论数
     */
    private Long commentTotal;
}
