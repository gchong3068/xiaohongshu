package com.gchong3068.xiaohongshu.user.biz.service;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.user.biz.model.vo.FindUserProfileReqVO;
import com.gchong3068.xiaohongshu.user.biz.model.vo.FindUserProfileRspVO;
import com.gchong3068.xiaohongshu.user.biz.model.vo.UpdateUserInfoReqVO;
import com.gchong3068.xiaohongshu.user.dto.req.*;
import com.gchong3068.xiaohongshu.user.dto.resp.FindUserByIdRspDTO;
import com.gchong3068.xiaohongshu.user.dto.resp.FindUserByPhoneRspDTO;

import java.util.List;

/**
 * @Description: 用户服务
 * @Author: gchong3068
 * @Date: 2024/7/3 19:07
 * @Version: v1.0.0
 **/
public interface UserService {

    /**
     * 更新用户信息
     * @Auther: gchong3068
     * @Date: 2024/7/3 19:37
     * @param updateUserInfoReqVO 用户信息修改VO
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> updateUserInfo(UpdateUserInfoReqVO updateUserInfoReqVO);

    /**
     * 用户注册
     * @Auther: gchong3068
     * @Date: 2024/7/8 23:05
     * @param registerUserReqDTO 用户注册请求DTO
     * @return com.gchong3068.framework.common.response.Response<java.lang.Long>
     **/
    Response<Long> register(RegisterUserReqDTO registerUserReqDTO);

    /**
     * 根据手机号查询
     * @Auther: gchong3068
     * @Date: 2024/7/9 23:02
     * @param findUserByPhoneReqDTO 手机号查询请求DTO
     * @return com.gchong3068.framework.common.response.Response<com.gchong3068.xiaohongshu.user.dto.resp.FindUserByPhoneRspDTO>
     **/
    Response<FindUserByPhoneRspDTO> findByPhone(FindUserByPhoneReqDTO findUserByPhoneReqDTO);


    /**
     * 更新密码
     * @Auther: gchong3068
     * @Date: 2024/7/10 22:09
     * @param updateUserPasswordReqDTO 更新密码请求DTO
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> updatePassword(UpdateUserPasswordReqDTO updateUserPasswordReqDTO);


    /***
     * 根据用户ID查询
     * @Auther: gchong3068
     * @Date: 2024/8/18 18:56
 	 * @param findUserByIdReqDTO 入参
 	 * @return com.gchong3068.framework.common.response.Response<com.gchong3068.xiaohongshu.user.dto.resp.FindUserByIdRspDTO>
     **/
    Response<FindUserByIdRspDTO> findById(FindUserByIdReqDTO findUserByIdReqDTO);

    /**
     * 批量根据用户ID 查询用户信息
     * @Auther: gchong3068
     * @Date: 2024/9/25 19:05
     * @param findUserByIdsReqDTO 入参
     * @return com.gchong3068.framework.common.response.Response<java.util.List < com.gchong3068.xiaohongshu.user.dto.req.FindUserByIdsReqDTO>>
     **/
    Response<List<FindUserByIdRspDTO>> findByIds(FindUserByIdsReqDTO findUserByIdsReqDTO);

    /**
     * 获取用户主页信息
     * @Auther: gchong3068
     * @Date: 2025/6/5 17:03
     * @param findUserProfileReqVO
     * @return com.gchong3068.framework.common.response.Response<com.gchong3068.xiaohongshu.user.biz.model.vo.FindUserProfileRspVO>
     **/
    Response<FindUserProfileRspVO> findUSerProfile(FindUserProfileReqVO findUserProfileReqVO);

}
