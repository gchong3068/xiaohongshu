package com.gchong3068.xiaohongshu.kv.biz.service;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.kv.dto.req.AddNoteContentReqDTO;
import com.gchong3068.xiaohongshu.kv.dto.req.DeleteNoteContentReqDTO;
import com.gchong3068.xiaohongshu.kv.dto.req.FindNoteContentReqDTO;
import com.gchong3068.xiaohongshu.kv.dto.rsp.FindNoteContentRspDTO;

/**
 * @Description: 笔记内容服务
 * @Author: gchong3068
 * @Date: 2024/7/28 22:13
 * @Version: v1.0.0
 **/
public interface NoteContentService {

    /**
     * 添加笔记内容
     * @Auther: gchong3068
     * @Date: 2024/7/28 22:15
 	 * @param addNoteContentReqDTO 笔记内容
 	 * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> addNoteContent(AddNoteContentReqDTO addNoteContentReqDTO);

    /**
     * 查询笔记内容
     * @Auther: gchong3068
     * @Date: 2024/7/29 21:43
     * @param findNoteContentReqDTO 查询
     * @return com.gchong3068.framework.common.response.Response<com.gchong3068.xiaohongshu.kv.dto.rsp.FindNoteContentRspDTO>
     **/
    Response<FindNoteContentRspDTO> findNoteContent(FindNoteContentReqDTO findNoteContentReqDTO);

    /**
     * 删除笔记
     * @Auther: gchong3068
     * @Date: 2024/7/29 21:54
     * @param deleteNoteContentReqDTO 删除
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> deleteNoteContent(DeleteNoteContentReqDTO deleteNoteContentReqDTO);

}
