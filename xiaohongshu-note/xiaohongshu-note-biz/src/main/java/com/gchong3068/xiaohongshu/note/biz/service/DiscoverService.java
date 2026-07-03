package com.gchong3068.xiaohongshu.note.biz.service;

import com.gchong3068.framework.common.response.PageResponse;
import com.gchong3068.xiaohongshu.note.biz.model.vo.FindDiscoverNotePageListReqVO;
import com.gchong3068.xiaohongshu.note.biz.model.vo.FindDiscoverNoteRspVO;

/**
 * @Description: 发现页业务
 * @Author: gchong3068
 * @Date: 2025/6/12 17:33
 * @Version: v1.0.0
 **/
public interface DiscoverService {

    PageResponse<FindDiscoverNoteRspVO> findNoteList(FindDiscoverNotePageListReqVO findDiscoverNoteListReqVO);
}
