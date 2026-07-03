package com.gchong3068.xiaohongshu.note.biz.service;

import com.gchong3068.framework.common.response.PageResponse;
import com.gchong3068.xiaohongshu.note.biz.model.vo.FindProfileNotePageListReqVO;
import com.gchong3068.xiaohongshu.note.biz.model.vo.FindProfileNoteRspVO;

/**
 * @Description: 个人主页业务
 * @Author: gchong3068
 * @Date: 2025/6/12 17:41
 * @Version: v1.0.0
 **/
public interface ProfileService {

    PageResponse<FindProfileNoteRspVO> findNoteList(FindProfileNotePageListReqVO findProfileNotePageListReqVO);
}
