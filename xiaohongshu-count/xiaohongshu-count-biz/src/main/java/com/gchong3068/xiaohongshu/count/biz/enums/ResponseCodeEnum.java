package com.gchong3068.xiaohongshu.count.biz.enums;

import com.gchong3068.framework.common.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 响应异常码
 * @Author: gchong3068
 * @Date: 2025/6/20 20:00
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {

    // ----------- 通用异常状态码 -----------
    SYSTEM_ERROR("COUNT-10000", "出错啦，后台小哥正在努力修复中..."),
    PARAM_NOT_VALID("COUNT-10001", "参数错误"),
    FLOW_LIMIT("COUNT-10002", "操作过于频繁"),

    // ----------- 业务异常状态码 -----------
    ;


    private final String errorCode;
    private final String errorMessage;

}
