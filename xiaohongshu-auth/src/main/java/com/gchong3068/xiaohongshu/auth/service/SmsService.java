package com.gchong3068.xiaohongshu.auth.service;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.auth.model.vo.veriticationcode.SendVerificationCodeReqVO;

/**
 * @Description: 短信服务
 * @Author: gchong3068
 * @Date: 2024/6/28 20:18
 * @Version: v1.0.0
 **/
public interface SmsService {

    Response<?> send(SendVerificationCodeReqVO sendVerificationCodeReqVO);
}
