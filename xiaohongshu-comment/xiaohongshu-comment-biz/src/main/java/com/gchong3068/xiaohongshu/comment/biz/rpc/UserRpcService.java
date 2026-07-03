package com.gchong3068.xiaohongshu.comment.biz.rpc;

import cn.hutool.core.collection.CollUtil;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.user.api.UserFeignApi;
import com.gchong3068.xiaohongshu.user.dto.req.FindUserByIdsReqDTO;
import com.gchong3068.xiaohongshu.user.dto.resp.FindUserByIdRspDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 用户服务
 * @Author: gchong3068
 * @Date: 2025/4/1 16:28
 * @Version: v1.0.0
 **/
@Component
public class UserRpcService {
    
    @Resource
    private UserFeignApi userFeignApi;
    
    /**
     * 批量查询用户信息
     * @Auther: gchong3068
     * @Date: 2025/4/1 16:32 
 	 * @param userIds
 	 * @return java.util.List<com.gchong3068.xiaohongshu.user.dto.resp.FindUserByIdRspDTO>
     **/
    public List<FindUserByIdRspDTO> findByIds(List<Long> userIds){
        
        if (CollUtil.isEmpty(userIds)){
            return null;
        }
        FindUserByIdsReqDTO findUserByIdsReqDTO = new FindUserByIdsReqDTO();
        
        findUserByIdsReqDTO.setIds(userIds.stream().distinct().collect(Collectors.toList()));

        Response<List<FindUserByIdRspDTO>> response = userFeignApi.findByIds(findUserByIdsReqDTO);
        
        if (response.isSuccess() || Objects.isNull(response.getData())|| CollUtil.isEmpty(response.getData())){
            return null;
        }
        return response.getData();
    }
}
