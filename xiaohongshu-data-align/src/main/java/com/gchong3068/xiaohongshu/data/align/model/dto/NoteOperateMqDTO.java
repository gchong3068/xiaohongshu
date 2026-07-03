package com.gchong3068.xiaohongshu.data.align.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 笔记操作
 * @Author: gchong3068
 * @Date: 2024/11/13 17:21
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoteOperateMqDTO {

    /**
     * 笔记发布者 ID
     */
    private Long creatorId;

    /**
     * 笔记 ID
     */
    private Long noteId;

    /**
     * 操作类型： 0 - 笔记删除； 1：笔记发布；
     */
    private Integer type;
}
