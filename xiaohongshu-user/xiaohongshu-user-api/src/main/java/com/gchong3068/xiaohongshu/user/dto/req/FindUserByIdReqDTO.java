package com.gchong3068.xiaohongshu.user.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 根据ID查询入参
 * @Author: gchong3068
 * @Date: 2024/8/18 18:52
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindUserByIdReqDTO {

    //手机号
    @NotNull(message = "用户 ID 不能为空")
    private Long id;
}
