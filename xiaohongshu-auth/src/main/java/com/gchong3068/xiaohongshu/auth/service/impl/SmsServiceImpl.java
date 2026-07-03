package com.gchong3068.xiaohongshu.auth.service.impl;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.auth.model.vo.veriticationcode.SendVerificationCodeReqVO;
import com.gchong3068.xiaohongshu.auth.service.SmsService;
import com.gchong3068.xiaohongshu.auth.strategy.SmsStrategy;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @Description: 短信服务实现类
 * @Author: gchong3068
 * @Date: 2024/6/28 20:18
 * @Version: v1.0.0
 **/
@Service
public class SmsServiceImpl implements SmsService {

    @Resource
    private SmsStrategy smsStrategy;
    @Override
    public Response<?> send(SendVerificationCodeReqVO sendVerificationCodeReqVO) {
        smsStrategy.send(sendVerificationCodeReqVO);
        return Response.success();
    }
}
