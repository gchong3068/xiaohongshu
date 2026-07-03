package com.gchong3068.xiaohongshu.note.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 取消收藏笔记请求参数
 * @Author: gchong3068
 * @Date: 2024/10/31 14:21
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnCollectNoteReqVO {

    @NotNull(message = "笔记ID不能为空")
    private Long id;

}
