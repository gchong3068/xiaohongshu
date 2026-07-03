package com.gchong3068.xiaohongshu.note.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 个人主页 - 已发布笔记列表
 * @Author: gchong3068
 * @Date: 2025/6/10 18:19
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindPublishedNoteListReqVO {


    @NotNull(message = "用户 ID 不能为空")
    private Long userId;

    /**
     * 游标，即笔记 ID，用于分页使用
     */
    private Long cursor;

}
