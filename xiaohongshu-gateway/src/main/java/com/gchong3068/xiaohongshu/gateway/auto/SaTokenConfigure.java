package com.gchong3068.xiaohongshu.gateway.auto;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * sa-token 配置
 * @author gc
 * @date 2024年06月13日 19:14
 */
@Configuration
@Slf4j
public class SaTokenConfigure {
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")    // 拦截全部path
                // 鉴权方法：每次访问进入
                .setAuth(ojb->{
                    log.info("==================> SaReactorFilter, Path: {}", SaHolder.getRequest().getRequestPath());
                    // 登录校验
                    SaRouter.match("/**") // 拦截所有路由
                            .notMatch("/auth/login") // 排除登录接口
                            .notMatch("/auth/verification/code/send") // 排除验证码发送接口
                            .notMatch("/user/user/profile") // 排除用户主页查看
                            .check(r -> StpUtil.checkLogin()) // 校验是否登录
                    ;
                })
                //异常处理
                .setError(e -> {
                    // return SaResult.error(e.getMessage());
                    // 手动抛出异常，抛给全局异常处理器
                    if (e instanceof NotLoginException) {
                        throw new NotLoginException(e.getMessage(), null, null);
                    } else if (e instanceof NotPermissionException || e instanceof NotRoleException) {
                        throw new NotPermissionException(e.getMessage());
                    } else {
                        throw new RuntimeException(e.getMessage());
                    }
                })
                ;
    }
}
