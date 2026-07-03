package com.gchong3068.xiaohongshu.search.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 重建笔记文档
 * @Author: gchong3068
 * @Date: 2024/12/19 16:31
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RebuildNoteDocumentReqDTO {

    @NotNull(message = "笔记ID不能为空")
    private Long id;

}
