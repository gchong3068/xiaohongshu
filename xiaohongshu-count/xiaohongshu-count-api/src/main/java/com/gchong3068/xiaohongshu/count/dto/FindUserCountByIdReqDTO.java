package com.gchong3068.xiaohongshu.count.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 查询用户维度相关计数
 * @Author: gchong3068
 * @Date: 2025/6/5 16:26
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindUserCountByIdReqDTO {

    /**
     * 用户 ID
     */
    @NotNull(message = "用户 ID 不能为空")
    private Long userId;
}
