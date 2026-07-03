package com.gchong3068.xiaohongshu.user.api;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.user.constant.ApiConstants;
import com.gchong3068.xiaohongshu.user.dto.req.*;
import com.gchong3068.xiaohongshu.user.dto.resp.FindUserByIdRspDTO;
import com.gchong3068.xiaohongshu.user.dto.resp.FindUserByPhoneRspDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description: 对外接口
 * @Author: gchong3068
 * @Date: 2024/7/8 23:18
 * @Version: v1.0.0
 **/
@FeignClient(name = ApiConstants.SERVICE_NAME)
public interface UserFeignApi {

    String PREFIX = "/user";


    /**
     * 用户注册
     * @Auther: gchong3068
     * @Date: 2024/9/25 19:40
     * @param registerUserReqDTO 注册请求参数
     * @return com.gchong3068.framework.common.response.Response<java.lang.Long>
     **/
    @PostMapping(value = PREFIX + "/register")
    Response<Long> register(@RequestBody RegisterUserReqDTO registerUserReqDTO);

    /**
     * 根据手机号查询
     * @Auther: gchong3068
     * @Date: 2024/9/25 19:40
     * @param findUserByPhoneReqDTO 手机号查询请求参数
     * @return com.gchong3068.framework.common.response.Response<com.gchong3068.xiaohongshu.user.dto.resp.FindUserByPhoneRspDTO>
     **/
    @PostMapping(value = PREFIX + "/findByPhone")
    Response<FindUserByPhoneRspDTO> findByPhone(@RequestBody FindUserByPhoneReqDTO findUserByPhoneReqDTO);


    /**
     * 更新密码
     * @Auther: gchong3068
     * @Date: 2024/9/25 19:39
     * @param updateUserPasswordReqDTO 更新密码请求参数
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @PostMapping(value = PREFIX + "/password/update")
    Response<?> updatePassword(@RequestBody UpdateUserPasswordReqDTO updateUserPasswordReqDTO);

    /**
     * 查询用户
     * @Auther: gchong3068
     * @Date: 2024/9/25 19:39
     * @param findUserByIdReqDTO 查询用户信息请求参数
     * @return com.gchong3068.framework.common.response.Response<com.gchong3068.xiaohongshu.user.dto.resp.FindUserByIdRspDTO>
     **/
    @PostMapping(value = PREFIX + "/findById")
    Response<FindUserByIdRspDTO> findById(@RequestBody FindUserByIdReqDTO findUserByIdReqDTO );
    /**
     * 批量查询
     * @Auther: gchong3068
     * @Date: 2024/9/25 19:38
 	 * @param findUserByIdsReqDTO 批量查询请求参数
 	 * @return com.gchong3068.framework.common.response.Response<java.util.List<com.gchong3068.xiaohongshu.user.dto.resp.FindUserByIdRspDTO>>
     **/
    @PostMapping(value = PREFIX + "/findByIds")
    Response<List<FindUserByIdRspDTO>> findByIds(@RequestBody FindUserByIdsReqDTO findUserByIdsReqDTO);
}

