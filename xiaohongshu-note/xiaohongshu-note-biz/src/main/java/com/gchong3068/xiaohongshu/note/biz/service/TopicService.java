package com.gchong3068.xiaohongshu.note.biz.service;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.note.biz.model.vo.FindTopicListReqVO;
import com.gchong3068.xiaohongshu.note.biz.model.vo.FindTopicRspVO;

import java.util.List;

/**
 * @Description:
 * @Author: gchong3068
 * @Date: 2025/6/12 17:59
 * @Version: v1.0.0
 **/
public interface TopicService {

    Response<List<FindTopicRspVO>> findTopicList(FindTopicListReqVO findTopicListReqVO);
}
