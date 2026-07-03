package com.gchong3068.xiaohongshu.auth.exception;

import com.gchong3068.framework.common.exception.BizException;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.auth.enums.ResponseCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

/**
 * @author gc
 * @date 2024年05月17日 21:00
 */

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获自定义业务异常
     * @Auther: gchong3068
     * @Date: 2024/6/21 18:05
 	 * @param request 入参
 	 * @param e 方法参数验证异常
 	 * @return com.gchong3068.framework.common.response.Response<java.lang.Object>
     **/
    @ExceptionHandler({ BizException.class })
    @ResponseBody
    public Response<Object> handleBizException(HttpServletRequest request, BizException e) {
        log.warn("{} request fail, errorCode: {}, errorMessage: {}", request.getRequestURI(), e.getErrorCode(), e.getErrorMessage());
        return Response.fail(e);
    }


    /**
     * 捕获参数校验异常
     * @Auther: gchong3068
     * @Date: 2024/6/21 18:03
     * @param request 入参
     * @param e 方法参数验证异常
     * @return com.gchong3068.framework.common.response.Response<java.lang.Object>
     **/
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Object> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e ) {
        // 参数错误异常码
        String errorCode = ResponseCodeEnum.PARAM_NOT_VALID.getErrorCode();

        //获取BindingResult
        BindingResult bindingResult = e.getBindingResult();

        StringBuffer sb = new StringBuffer();
        // 获取校验不通过的字段，并组合错误信息，格式为： email 邮箱格式不正确, 当前值: '123124qq.com';
        Optional.of(bindingResult.getFieldErrors()).ifPresent(errors -> errors.forEach(error ->
                sb.append(error.getField())
                        .append(" ")
                        .append(error.getDefaultMessage())
                        .append(", 当前值: '")
                        .append(error.getRejectedValue())
                        .append("'; ")

        ));

        String errorMessage = sb.toString();

        log.warn("{} request error, errorCode: {}, errorMessage: {}", request.getRequestURI(), errorCode, errorMessage);

        return Response.fail(errorCode, errorMessage);
    }

    /**
     * 其他异常
     * @author gc
     * @date 2024/5/17 21:17
     * @param request 入参
     * @param e 异常
     * @return com.gchong3068.framework.common.response.Response<java.lang.Object>
     */

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public Response<Object> handleException(HttpServletRequest request, Exception e) {
        log.error("{} request error , " , request.getRequestURI(),e);
        return Response.fail(ResponseCodeEnum.SYSTEM_ERROR);
    }

    /**
     * Guava 参数校验异常
     * @Auther: gchong3068
     * @Date: 2024/6/21 18:10
 	 * @param request 入参
 	 * @param e 非法参数异常
 	 * @return com.gchong3068.framework.common.response.Response<java.lang.Object>
     **/
    @ExceptionHandler({IllegalArgumentException.class})
    public Response<Object> handIllegalArgumentException(HttpServletRequest request, IllegalArgumentException e) {
        String errorCode = ResponseCodeEnum.PARAM_NOT_VALID.getErrorCode();

        //错误信息
        String errorMessage = e.getMessage();

        log.warn("{} request error, errorCode: {}, errorMessage: {}", request.getRequestURI(), errorCode, errorMessage);

        return Response.fail(errorCode, errorMessage);
    }
}
