package com.gchong3068.xiaohongshu.count.biz.server;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.count.dto.FindNoteCountsByIdRspDTO;
import com.gchong3068.xiaohongshu.count.dto.FindNoteCountsByIdsReqDTO;

import java.util.List;

/**
 * @Description: 笔记计数
 * @Author: gchong3068
 * @Date: 2025/6/10 18:59
 * @Version: v1.0.0
 **/
public interface NoteCountService {

    /**
     * 批量查询笔记计数
     * @Auther: gchong3068
     * @Date: 2025/6/10 19:00
     * @param findNoteCountsByIdsReqDTO
     * @return com.gchong3068.framework.common.response.Response<com.gchong3068.xiaohongshu.count.dto.FindNoteCountsByIdRspDTO>
     **/
    Response<List<FindNoteCountsByIdRspDTO>> findNotesCountData(FindNoteCountsByIdsReqDTO findNoteCountsByIdsReqDTO);
}
