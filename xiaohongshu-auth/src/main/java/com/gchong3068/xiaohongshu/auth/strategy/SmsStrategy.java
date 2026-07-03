package com.gchong3068.xiaohongshu.auth.strategy;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.auth.model.vo.veriticationcode.SendVerificationCodeReqVO;

/**
 * @Description: 短信策略
 * @Author: gchong3068
 * @Date: 2024/6/28 20:07
 * @Version: v1.0.0
 **/
public interface SmsStrategy {

    /**
     * 发送验证码
     * @Auther: gchong3068
     * @Date: 2024/6/28 20:09
 	 * @param sendVerificationCodeReqVO 短信VO
 	 * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> send(SendVerificationCodeReqVO sendVerificationCodeReqVO);
}
