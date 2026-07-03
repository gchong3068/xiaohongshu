package com.gchong3068.xiaohongshu.note.biz.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 获取笔记是否点赞、收藏数据
 * @Author: gchong3068
 * @Date: 2025/6/5 16:12
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindNoteIsLikedAndCollectedRspVO {

    /**
     * 笔记 ID
     */
    private Long noteId;

    /**
     * 是否被当前登录的用户点赞
     */
    private Boolean isLiked;

    /**
     * 是否被当前登录的用户收藏
     */
    private Boolean isCollected;
}
