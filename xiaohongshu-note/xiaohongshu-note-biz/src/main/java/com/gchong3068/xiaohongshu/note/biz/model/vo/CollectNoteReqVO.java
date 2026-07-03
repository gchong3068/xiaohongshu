package com.gchong3068.xiaohongshu.note.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 收藏笔记
 * @Author: gchong3068
 * @Date: 2024/10/27 21:23
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectNoteReqVO {

    @NotNull(message = "笔记 ID 不能为空")
    private Long id;

}
