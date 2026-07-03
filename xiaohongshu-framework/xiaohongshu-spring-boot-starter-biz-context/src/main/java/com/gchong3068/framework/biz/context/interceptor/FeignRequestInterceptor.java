package com.gchong3068.framework.biz.context.interceptor;

import com.gchong3068.framework.biz.context.holder.LoginUserContextHolder;
import com.gchong3068.framework.common.constant.GlobalConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @Description: Feign请求拦截器
 * @Author: gchong3068
 * @Date: 2024/7/7 21:39
 * @Version: v1.0.0
 **/
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        Long userId = LoginUserContextHolder.getUserId();

        if (Objects.nonNull(userId)){
            requestTemplate.header(GlobalConstants.USER_ID,String.valueOf(userId));
            log.info("########## feign 请求设置请求头 userId: {}", userId);
        }
    }
}
