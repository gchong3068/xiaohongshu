package com.gchong3068.xiaohongshu.note.biz.rpc;

import cn.hutool.core.collection.CollUtil;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.count.api.CountFeignApi;
import com.gchong3068.xiaohongshu.count.dto.FindNoteCountsByIdRspDTO;
import com.gchong3068.xiaohongshu.count.dto.FindNoteCountsByIdsReqDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @Description: 计数服务
 * @Author: gchong3068
 * @Date: 2025/6/11 15:39
 * @Version: v1.0.0
 **/
@Component
public class CountRpcService {

    @Resource
    private CountFeignApi countFeignApi;


    /**
     * 批量查询笔记计数
     * @Auther: gchong3068
     * @Date: 2025/6/11 21:37
     * @param noteIds
     * @return java.util.List<com.gchong3068.xiaohongshu.count.dto.FindNoteCountsByIdRspDTO>
     **/
    public List<FindNoteCountsByIdRspDTO> findByNoteIds(List<Long> noteIds){
        FindNoteCountsByIdsReqDTO findNoteCountsByIdsReqDTO = new FindNoteCountsByIdsReqDTO();
        findNoteCountsByIdsReqDTO.setNoteIds(noteIds);

        Response<List<FindNoteCountsByIdRspDTO>> response = countFeignApi.findNotesCount(findNoteCountsByIdsReqDTO);

        if (!response.isSuccess() || Objects.isNull(response.getData()) || CollUtil.isEmpty(response.getData())){
            return null;
        }

        return response.getData();
    }
}

