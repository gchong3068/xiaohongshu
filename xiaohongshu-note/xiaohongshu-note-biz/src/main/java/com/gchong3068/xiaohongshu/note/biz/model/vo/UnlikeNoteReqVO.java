package com.gchong3068.xiaohongshu.note.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 取消点赞
 * @Author: gchong3068
 * @Date: 2024/10/22 18:36
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnlikeNoteReqVO {

    @NotNull(message = "笔记 ID 不能为空")
    private Long id;

}
