package com.gchong3068.xiaohongshu.count.biz.server;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.count.dto.FindUserCountByIdReqDTO;
import com.gchong3068.xiaohongshu.count.dto.FindUserCountByIdRspDTO;

/**
 * @Description: 用户计数服务
 * @Author: gchong3068
 * @Date: 2025/6/5 16:32
 * @Version: v1.0.0
 **/
public interface UserCountService {

    /**
     * 查询用户相关计数
     * @Auther: gchong3068
     * @Date: 2025/6/5 16:34
     * @param findUserCountByIdReqDTO
     * @return com.gchong3068.framework.common.response.Response<com.gchong3068.xiaohongshu.count.dto.FindUserCountByIdRspDTO>
     **/
    Response<FindUserCountByIdRspDTO> findUserCountData(FindUserCountByIdReqDTO findUserCountByIdReqDTO);

}
