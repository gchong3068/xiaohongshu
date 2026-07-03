package com.gchong3068.xiaohongshu.note.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 点赞
 * @Author: gchong3068
 * @Date: 2024/10/17 10:17
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeNoteReqVO {

    @NotNull(message = "笔记ID 不能为空")
    private Long id;

}
