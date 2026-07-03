package com.gchong3068.xiaohongshu.auth.strategy.impl;

import cn.hutool.core.util.RandomUtil;
import com.gchong3068.framework.common.exception.BizException;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.auth.constant.RedisKeyConstants;
import com.gchong3068.xiaohongshu.auth.enums.ResponseCodeEnum;
import com.gchong3068.xiaohongshu.auth.model.vo.veriticationcode.SendVerificationCodeReqVO;
import com.gchong3068.xiaohongshu.auth.sms.AliyunSmsHelper;
import com.gchong3068.xiaohongshu.auth.strategy.SmsStrategy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.TimeUnit;

/**
 * @Description: AliyunSms策略
 * @Author: gchong3068
 * @Date: 2024/6/28 20:10
 * @Version: v1.0.0
 **/
@Slf4j
public class AliyunSmsStrategy implements SmsStrategy {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private AliyunSmsHelper aliyunSmsHelper;


    @Override
    public Response<?> send(SendVerificationCodeReqVO sendVerificationCodeReqVO) {
        String phone = sendVerificationCodeReqVO.getPhone();
        String key = RedisKeyConstants.buildVerificationCodeKey(phone);
        Boolean isSend = redisTemplate.hasKey(key);
        if (Boolean.TRUE.equals(isSend)) {
            throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_SEND_FREQUENTLY);
        }
        String verificationCode = RandomUtil.randomNumbers(6);

        log.info("==> 阿里云短信发送 ...");
        log.info("==> 手机号: {}, 已发送验证码：【{}】" , phone, verificationCode);

        threadPoolTaskExecutor.submit(()->{
            String signName = "阿里云短信测试";
            String templateCode = "SMS_154950909";
            String templateParam  = String.format("{\"code\":\"%s\"}", verificationCode);
            aliyunSmsHelper.sendMessage(signName,templateCode,phone,templateParam);
        });

        redisTemplate.opsForValue().set(key,verificationCode,3, TimeUnit.MINUTES);

        return Response.success();
    }
}
