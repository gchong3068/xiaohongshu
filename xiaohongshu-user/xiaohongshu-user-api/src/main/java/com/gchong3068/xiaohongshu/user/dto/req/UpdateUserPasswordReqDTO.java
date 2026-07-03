package com.gchong3068.xiaohongshu.user.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 更新密码请求
 * @Author: gchong3068
 * @Date: 2024/7/10 22:07
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserPasswordReqDTO {

    @NotBlank(message = "密码不能为空")
    private String encodePassword;

}
