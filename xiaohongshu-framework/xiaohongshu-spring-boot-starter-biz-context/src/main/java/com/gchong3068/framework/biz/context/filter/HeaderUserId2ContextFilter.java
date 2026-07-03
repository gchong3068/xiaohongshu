package com.gchong3068.framework.biz.context.filter;

import com.gchong3068.framework.biz.context.holder.LoginUserContextHolder;
import com.gchong3068.framework.common.constant.GlobalConstants;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @Description: 提取请求头中的用户 ID
 * @Author: gchong3068
 * @Date: 2024/6/24 17:26
 * @Version: v1.0.0
 **/
@Slf4j
public class HeaderUserId2ContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader(GlobalConstants.USER_ID);

        if (StringUtils.isBlank(userId)){
            filterChain.doFilter(request,response);
            return;
        }

        log.info("===== 设置 userId 到 ThreadLocal 中， 用户 ID: {}", userId);
        LoginUserContextHolder.setUserId(userId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // 删除 ThreadLocal ，防止内存泄露
            LoginUserContextHolder.remove();
            log.info("===== 删除 ThreadLocal， userId: {}", userId);
        }
    }
}
