package com.gchong3068.xiaohongshu.auth.model.vo.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 修改密码
 * @Author: gchong3068
 * @Date: 2024/6/25 18:24
 * @Version: v1.0.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdatePasswordReqVO {

    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
