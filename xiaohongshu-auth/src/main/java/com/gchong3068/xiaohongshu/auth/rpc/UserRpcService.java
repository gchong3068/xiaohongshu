package com.gchong3068.xiaohongshu.auth.rpc;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.user.api.UserFeignApi;
import com.gchong3068.xiaohongshu.user.dto.req.FindUserByPhoneReqDTO;
import com.gchong3068.xiaohongshu.user.dto.req.RegisterUserReqDTO;
import com.gchong3068.xiaohongshu.user.dto.req.UpdateUserPasswordReqDTO;
import com.gchong3068.xiaohongshu.user.dto.resp.FindUserByPhoneRspDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Description: 用户RPC服务
 * @Author: gchong3068
 * @Date: 2024/7/8 23:22
 * @Version: v1.0.0
 **/
@Component
public class UserRpcService {

    @Resource
    private UserFeignApi userFeignApi;

    public Long registerUser(String phone) {
        RegisterUserReqDTO registerUserReqDTO = new RegisterUserReqDTO();

        registerUserReqDTO.setPhone(phone);

        Response<Long> response = userFeignApi.register(registerUserReqDTO);

        if (!response.isSuccess()){
            return null;
        }

        return response.getData();
    }

    /**
     * 根据手机号查询
     * @Auther: gchong3068
     * @Date: 2024/7/9 23:11
 	 * @param phone 手机号
 	 * @return com.gchong3068.xiaohongshu.user.dto.resp.FindUserByPhoneRspDTO
     **/
    public FindUserByPhoneRspDTO findUserByPhoneRspDTO(String phone){
        FindUserByPhoneReqDTO findUserByPhoneReqDTO = new FindUserByPhoneReqDTO();
        findUserByPhoneReqDTO.setPhone(phone);

        Response<FindUserByPhoneRspDTO> response = userFeignApi.findByPhone(findUserByPhoneReqDTO);

        if (!response.isSuccess()){
            return null;
        }
        return response.getData();
    }


    /**
     * 密码更新
     * @Auther: gchong3068
     * @Date: 2024/7/10 22:17
     * @param encodePassword 密码
     **/
    public void updatePassword(String encodePassword){
        UpdateUserPasswordReqDTO updateUserPasswordReqDTO = new UpdateUserPasswordReqDTO();

        updateUserPasswordReqDTO.setEncodePassword(encodePassword);
        userFeignApi.updatePassword(updateUserPasswordReqDTO);
    }
}
