package com.gchong3068.xiaohongshu.oss.biz.enums;

import com.gchong3068.framework.common.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 异常码
 * @Author: gchong3068
 * @Date: 2024/7/1 22:37
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {

    SYSTEM_ERROR("OSS-10000", "出错啦，后台小哥正在努力修复中..."),
    PARAM_NOT_VALID("OSS-10001", "参数错误"),
    ;


    private final String errorCode;
    private final String errorMessage;


}
