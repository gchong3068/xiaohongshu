package com.gchong3068.xiaohongshu.auth.service;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.auth.model.vo.user.UpdatePasswordReqVO;
import com.gchong3068.xiaohongshu.auth.model.vo.user.UserLoginReqVO;

/**
 * @author gc
 * @date 2024年05月30日 23:18
 */
public interface AuthService {

    /**
     * 登录或注册
     * @author gc
     * @date 2024/5/30 23:20
     * @param userLoginReqVO 登录VO
     * @return com.gchong3068.framework.common.response.Response<java.lang.String>
     */
    Response<String > loginAndRegister(UserLoginReqVO userLoginReqVO);


    /**
     * 登出
     * @Auther: gchong3068
     * @Date: 2024/6/21 17:55
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> logout();
    /**
     * 修改密码
     * @Auther: gchong3068
     * @Date: 2024/6/25 18:34
     * @param updatePasswordReqVO 修改密码VO
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> UpdatePassword(UpdatePasswordReqVO updatePasswordReqVO);
}
