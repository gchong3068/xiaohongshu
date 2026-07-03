package com.gchong3068.xiaohongshu.user.dto.req;

import com.gchong3068.framework.common.validator.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 用户注册
 * @Author: gchong3068
 * @Date: 2024/7/8 22:59
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserReqDTO {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @PhoneNumber
    private String phone;

}
