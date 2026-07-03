package com.gchong3068.xiaohongshu.auth.controller;

import com.gchong3068.framework.biz.operationlog.aspect.ApiOperationLog;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.auth.model.vo.user.UpdatePasswordReqVO;
import com.gchong3068.xiaohongshu.auth.model.vo.user.UserLoginReqVO;
import com.gchong3068.xiaohongshu.auth.service.AuthService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author gc
 * @date 2024年05月30日 23:31
 */
@RestController
@Slf4j
public class AuthController {
    @Resource
    private AuthService authService;


    /**
     * 登录
     * @Auther: gchong3068
     * @Date: 2024/6/25 18:32
     * @param userLoginReqVO 登录VO
     * @return com.gchong3068.framework.common.response.Response<java.lang.String>
     **/
    @PostMapping("/login")
    @ApiOperationLog(description = "用户登录/注册")
    public Response<String> loginAndRegister(@Validated @RequestBody UserLoginReqVO userLoginReqVO) {
        return authService.loginAndRegister(userLoginReqVO);
    }

    /**
     * 登出
     * @Auther: gchong3068
     * @Date: 2024/6/25 18:31
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @PostMapping("/logout")
    @ApiOperationLog(description = "账号登出")
    public Response<?> logout() {
        return authService.logout();
    }


    /**
     * 修改密码
     * @Auther: gchong3068
     * @Date: 2024/6/25 18:40
     * @param updatePasswordReqVO 修改密码VO
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @PostMapping("/password/update")
    @ApiOperationLog(description = "修改密码")
    public Response<?> updatePassword(@Validated @RequestBody UpdatePasswordReqVO updatePasswordReqVO){
        return authService.UpdatePassword(updatePasswordReqVO);
    }
}
