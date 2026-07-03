package com.gchong3068.xiaohongshu.gateway.enums;

import com.gchong3068.framework.common.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 异常码枚举
 * @Author: gchong3068
 * @Date: 2024/6/17 21:59
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {


    SYSTEM_ERROR("500","系统繁忙，请稍后再试"),
    UNAUTHORIZED("401","授权不足"),


    ;

    private final String errorCode;
    private final String errorMessage;

}
