package com.gchong3068.xiaohongshu.note.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 获取笔记是否点赞、收藏数据
 * @Author: gchong3068
 * @Date: 2025/6/5 16:11
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindNoteISLikedAndCollectedReqVO {
    @NotNull(message = "笔记 ID 不能为空")
    private Long noteId;
}
