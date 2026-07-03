package com.gchong3068.xiaohongshu.search.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 重建用户文档
 * @Author: gchong3068
 * @Date: 2024/12/19 17:03
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RebuildUserDocumentReqDTO {

    @NotNull(message = "用户 ID 不能为空")
    private Long id;
}
