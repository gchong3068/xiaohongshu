package com.gchong3068.xiaohongshu.count.api;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.count.constant.ApiConstants;
import com.gchong3068.xiaohongshu.count.dto.FindNoteCountsByIdRspDTO;
import com.gchong3068.xiaohongshu.count.dto.FindNoteCountsByIdsReqDTO;
import com.gchong3068.xiaohongshu.count.dto.FindUserCountByIdReqDTO;
import com.gchong3068.xiaohongshu.count.dto.FindUserCountByIdRspDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description: TODO
 * @Author: gchong3068
 * @Date: 2025/6/5 16:59
 * @Version: v1.0.0
 **/
@FeignClient(name = ApiConstants.SERVICE_NAME)
public interface CountFeignApi {

    String PREFIX = "/count";

    /**
     * 查询用户计数
     * @Auther: gchong3068
     * @Date: 2025/6/5 17:02
     * @param findUserCountByIdReqDTO
     * @return com.gchong3068.framework.common.response.Response<com.gchong3068.xiaohongshu.count.dto.FindUserCountByIdRspDTO>
     **/
    @PostMapping(value = PREFIX + "/user/data")
    Response<FindUserCountByIdRspDTO> findUserCount(@RequestBody FindUserCountByIdReqDTO findUserCountByIdReqDTO);


    /**
     * 批量查询笔记计数
     * @Auther: gchong3068
     * @Date: 2025/6/11 15:37
     * @param findNoteCountsByIdsReqDTO
     * @return com.gchong3068.framework.common.response.Response<java.util.List < com.gchong3068.xiaohongshu.count.dto.FindNoteCountsByIdRspDTO>>
     **/
    @PostMapping(value = PREFIX + "/notes/data")
    Response<List<FindNoteCountsByIdRspDTO>> findNotesCount(@RequestBody FindNoteCountsByIdsReqDTO findNoteCountsByIdsReqDTO);

}
