package com.gchong3068.xiaohongshu.search.biz.service;

import com.gchong3068.framework.common.response.PageResponse;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.search.biz.model.vo.SearchNoteReqVO;
import com.gchong3068.xiaohongshu.search.biz.model.vo.SearchNoteRspVO;
import com.gchong3068.xiaohongshu.search.dto.RebuildNoteDocumentReqDTO;

/**
 * @Description: 笔记服务
 * @Author: gchong3068
 * @Date: 2024/12/6 13:30
 * @Version: v1.0.0
 **/
public interface NoteService {

    /**
     * 搜索笔记
     * @Auther: gchong3068
     * @Date: 2024/12/6 13:32
     * @param searchNoteReqVO 搜索笔记入参
     * @return com.gchong3068.framework.common.response.PageResponse<com.gchong3068.xiaohongshu.search.model.vo.SearchNoteRspVO>
     **/
    PageResponse<SearchNoteRspVO> searchNote(SearchNoteReqVO searchNoteReqVO);

    /**
     * 重建笔记文档
     * @Auther: gchong3068
     * @Date: 2024/12/19 16:57
 	 * @param rebuildNoteDocumentReqDTO 笔记重建入参
 	 * @return com.gchong3068.framework.common.response.Response<java.lang.Long>
     **/
    Response<Long> rebuildDocument(RebuildNoteDocumentReqDTO rebuildNoteDocumentReqDTO);








}
