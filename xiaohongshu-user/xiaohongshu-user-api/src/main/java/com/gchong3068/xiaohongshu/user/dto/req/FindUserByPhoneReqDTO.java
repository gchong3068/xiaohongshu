package com.gchong3068.xiaohongshu.user.dto.req;

import com.gchong3068.framework.common.validator.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 手机号查询
 * @Author: gchong3068
 * @Date: 2024/7/9 22:56
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindUserByPhoneReqDTO {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @PhoneNumber
    private String phone;

}
