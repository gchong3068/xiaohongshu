package com.gchong3068.xiaohongshu.gateway.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.gateway.enums.ResponseCodeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description: 全局异常
 * @Author: gchong3068
 * @Date: 2024/6/17 22:03
 * @Version: v1.0.0
 **/
@Component
@Slf4j
public class GlobalExceptionHandle implements ErrorWebExceptionHandler {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        ServerHttpResponse response = exchange.getResponse();

        log.error("==>全局异常捕获: ",ex);

        Response<?> result;
        if (ex instanceof NotLoginException){  //SaToken 异常
            response.setStatusCode(HttpStatus.UNAUTHORIZED);  //401状态码
            result = Response.fail(ResponseCodeEnum.UNAUTHORIZED.getErrorCode(),"未携带 Token 令牌");

        } else if (ex instanceof NotPermissionException) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            result = Response.fail(ResponseCodeEnum.UNAUTHORIZED.getErrorCode(),ResponseCodeEnum.SYSTEM_ERROR.getErrorMessage());
        } else {
            result = Response.fail(ResponseCodeEnum.SYSTEM_ERROR);
        }
        // 设置响应头的内容类型为 application/json;charset=UTF-8，表示响应体为 JSON 格式
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        //设置body 响应
        return response.writeWith(Mono.fromSupplier(()->{
            DataBufferFactory  bufferFactory = response.bufferFactory();
            try {
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(result));
            } catch (Exception e) {
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}
