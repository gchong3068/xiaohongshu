package com.gchong3068.xiaohongshu.auth.controller;

import com.gchong3068.framework.biz.operationlog.aspect.ApiOperationLog;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.auth.model.vo.veriticationcode.SendVerificationCodeReqVO;
import com.gchong3068.xiaohongshu.auth.service.SmsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gc
 * @date 2024年05月22日 21:23
 */
@RestController
@Slf4j
public class VerificationCodeController {

    @Resource
    private SmsService smsService;

    @PostMapping("/verification/code/send")
    @ApiOperationLog(description = "发送验证码")
    public Response<?> send(@Validated @RequestBody SendVerificationCodeReqVO sendVerificationCodeReqVO){
        return smsService.send(sendVerificationCodeReqVO);
    }
}
