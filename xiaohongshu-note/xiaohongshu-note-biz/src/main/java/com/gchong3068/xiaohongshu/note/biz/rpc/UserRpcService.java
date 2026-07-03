package com.gchong3068.xiaohongshu.note.biz.rpc;

import cn.hutool.core.collection.CollUtil;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.user.api.UserFeignApi;
import com.gchong3068.xiaohongshu.user.dto.req.FindUserByIdReqDTO;
import com.gchong3068.xiaohongshu.user.dto.req.FindUserByIdsReqDTO;
import com.gchong3068.xiaohongshu.user.dto.resp.FindUserByIdRspDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 用户RPC 服务
 * @Author: gchong3068
 * @Date: 2024/8/22 21:45
 * @Version: v1.0.0
 **/
@Component
public class UserRpcService {

    @Resource
    private UserFeignApi userFeignApi;


    public FindUserByIdRspDTO findById(Long userId){
        FindUserByIdReqDTO findUserByIdReqDTO = new FindUserByIdReqDTO();
        findUserByIdReqDTO.setId(userId);

        Response<FindUserByIdRspDTO> response = userFeignApi.findById(findUserByIdReqDTO);
        if (Objects.isNull(response) || !response.isSuccess()){
            return null;
        }
        return response.getData();
    }


    /**
     * 批量查询用户信息
     *
     * @param userIds
     * @return
     */
    public List<FindUserByIdRspDTO> findByIds(List<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return null;
        }

        FindUserByIdsReqDTO findUsersByIdsReqDTO = new FindUserByIdsReqDTO();
        // 去重, 并设置用户 ID 集合
        findUsersByIdsReqDTO.setIds(userIds.stream().distinct().collect(Collectors.toList()));

        Response<List<FindUserByIdRspDTO>> response = userFeignApi.findByIds(findUsersByIdsReqDTO);

        if (!response.isSuccess() || Objects.isNull(response.getData()) || CollUtil.isEmpty(response.getData())) {
            return null;
        }

        return response.getData();
    }
}
