package com.gchong3068.xiaohongshu.user.biz.rpc;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.count.api.CountFeignApi;
import com.gchong3068.xiaohongshu.count.dto.FindUserCountByIdReqDTO;
import com.gchong3068.xiaohongshu.count.dto.FindUserCountByIdRspDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Description: 计数服务
 * @Author: gchong3068
 * @Date: 2025/6/5 17:12
 * @Version: v1.0.0
 **/
@Component
public class CountRpcService {

    @Resource
    private CountFeignApi countFeignApi;

    /**
     * 查询用户计数信息
     * @Auther: gchong3068
     * @Date: 2025/6/5 17:19
     * @param userId
     * @return com.gchong3068.xiaohongshu.count.dto.FindUserCountByIdRspDTO
     **/
    public FindUserCountByIdRspDTO findUserCountById(Long userId){
        FindUserCountByIdReqDTO findUserCountsByIdReqDTO = new FindUserCountByIdReqDTO();
        findUserCountsByIdReqDTO.setUserId(userId);

        Response<FindUserCountByIdRspDTO> response = countFeignApi.findUserCount(findUserCountsByIdReqDTO);

        if (Objects.isNull(response) || !response.isSuccess()) {
            return null;
        }

        return response.getData();
    }
}
