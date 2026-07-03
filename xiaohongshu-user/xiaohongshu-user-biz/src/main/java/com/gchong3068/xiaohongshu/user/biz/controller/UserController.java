package com.gchong3068.xiaohongshu.user.biz.controller;

import com.gchong3068.framework.biz.operationlog.aspect.ApiOperationLog;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.user.biz.model.vo.FindUserProfileReqVO;
import com.gchong3068.xiaohongshu.user.biz.model.vo.FindUserProfileRspVO;
import com.gchong3068.xiaohongshu.user.biz.model.vo.UpdateUserInfoReqVO;
import com.gchong3068.xiaohongshu.user.biz.service.UserService;
import com.gchong3068.xiaohongshu.user.dto.req.*;
import com.gchong3068.xiaohongshu.user.dto.resp.FindUserByIdRspDTO;
import com.gchong3068.xiaohongshu.user.dto.resp.FindUserByPhoneRspDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: 用户
 * @Author: gchong3068
 * @Date: 2024/7/3 19:03
 * @Version: v1.0.0
 **/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;


    /**
     * 用户信息修改
     * @Auther: gchong3068
     * @Date: 2024/7/3 19:47
     * @param updateUserInfoReqVO 修改信息VO
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<?> updateUserInfo(@Validated UpdateUserInfoReqVO updateUserInfoReqVO) {
        return userService.updateUserInfo(updateUserInfoReqVO);
    }

    // ===================================== 对其他服务提供的接口 =====================================

    @PostMapping("/register")
    @ApiOperationLog(description = "用户注册")
    public Response<Long> register(@Validated @RequestBody RegisterUserReqDTO registerUserReqDTO) {
        return userService.register(registerUserReqDTO);
    }

    @PostMapping("/findByPhone")
    @ApiOperationLog(description = "手机号查询用户信息")
    public Response<FindUserByPhoneRspDTO> findByPhone(@Validated @RequestBody FindUserByPhoneReqDTO findUserByPhoneReqDTO){
        return userService.findByPhone(findUserByPhoneReqDTO);
    }

    @PostMapping("/password/update")
    @ApiOperationLog(description = "更新密码")
    public Response<?> updatePassword(@Validated @RequestBody UpdateUserPasswordReqDTO updateUserPasswordReqDTO){
        return userService.updatePassword(updateUserPasswordReqDTO);
    }

    @PostMapping("/findById")
    @ApiOperationLog(description = "查询用户信息")
    public Response<FindUserByIdRspDTO> findById(@Validated @RequestBody FindUserByIdReqDTO findUserByIdReqDTO){
        return userService.findById(findUserByIdReqDTO);
    }

    @PostMapping("/findByIds")
    @ApiOperationLog(description = "批量查询用户信息")
    public Response<List<FindUserByIdRspDTO>> findByIds(@Validated @RequestBody FindUserByIdsReqDTO findUserByIdsReqDTO){
        return userService.findByIds(findUserByIdsReqDTO);
    }
    @PostMapping(value = "/profile")
    public Response<FindUserProfileRspVO> findUserProfile(@Validated @RequestBody FindUserProfileReqVO findUserProfileReqVO){
        return userService.findUSerProfile(findUserProfileReqVO);
    }
}
