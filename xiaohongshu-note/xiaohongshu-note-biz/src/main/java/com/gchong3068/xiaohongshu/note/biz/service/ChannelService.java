package com.gchong3068.xiaohongshu.note.biz.service;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.note.biz.model.vo.FindChannelRspVO;

import java.util.List;

/**
 * @Description:
 * @Author: gchong3068
 * @Date: 2025/6/12 17:27
 * @Version: v1.0.0
 **/
public interface ChannelService {


    /**
     * 查询所有频道
     * @Auther: gchong3068
     * @Date: 2025/6/12 17:29
     * @return com.gchong3068.framework.common.response.Response<java.util.List < com.gchong3068.xiaohongshu.note.biz.model.vo.FindChannelRspVO>>
     **/
    Response<List<FindChannelRspVO>> findChannelList();
}
