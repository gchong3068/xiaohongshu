package com.gchong3068.xiaohongshu.user.relation.biz.rpc;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.user.api.UserFeignApi;
import com.gchong3068.xiaohongshu.user.dto.req.FindUserByIdReqDTO;
import com.gchong3068.xiaohongshu.user.dto.req.FindUserByIdsReqDTO;
import com.gchong3068.xiaohongshu.user.dto.resp.FindUserByIdRspDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @Description: 用户RPC 服务
 * @Author: gchong3068
 * @Date: 2024/9/9 21:03
 * @Version: v1.0.0
 **/
@Component
public class UserRpcService {

    @Resource
    private UserFeignApi userFeignApi;

    /**
     * 根据用户ID查询
     * @Auther: gchong3068
     * @Date: 2024/9/9 21:24
 	 * @param userId 用户ID
 	 * @return com.gchong3068.xiaohongshu.user.dto.resp.FindUserByIdRspDTO
     **/
    public FindUserByIdRspDTO findById(Long userId){
        FindUserByIdReqDTO findUserByIdReqDTO = new FindUserByIdReqDTO();
        findUserByIdReqDTO.setId(userId);

        Response<FindUserByIdRspDTO> response = userFeignApi.findById(findUserByIdReqDTO);
        if (!response.isSuccess() || Objects.isNull(response.getData())){
            return null;
        }
        return response.getData();
    }

    /**
     * 批量查询用户信息
     * @Auther: gchong3068
     * @Date: 2024/9/25 21:32
     * @param userIds 用户ID集合
     * @return java.util.List<com.gchong3068.xiaohongshu.user.dto.resp.FindUserByIdRspDTO>
     **/
    public List<FindUserByIdRspDTO> findByIds(List<Long> userIds){
        FindUserByIdsReqDTO findUserByIdsReqDTO = new FindUserByIdsReqDTO();
        findUserByIdsReqDTO.setIds(userIds);

        Response<List<FindUserByIdRspDTO>> response = userFeignApi.findByIds(findUserByIdsReqDTO);

        if (!response.isSuccess() || Objects.isNull(response.getData())){
            return null;
        }
        return response.getData();
     }
}
