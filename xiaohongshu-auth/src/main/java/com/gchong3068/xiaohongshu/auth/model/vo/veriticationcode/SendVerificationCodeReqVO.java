package com.gchong3068.xiaohongshu.auth.model.vo.veriticationcode;

import com.gchong3068.framework.common.validator.PhoneNumber;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gc
 * @date 2024年05月22日 21:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendVerificationCodeReqVO {
    @NotNull(message = "手机号不能为空")
    @PhoneNumber
    private String phone;
}
