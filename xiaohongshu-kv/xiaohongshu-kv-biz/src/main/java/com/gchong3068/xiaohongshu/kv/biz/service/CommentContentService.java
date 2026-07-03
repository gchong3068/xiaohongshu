package com.gchong3068.xiaohongshu.kv.biz.service;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.kv.dto.req.BatchAddCommentContentReqDTO;
import com.gchong3068.xiaohongshu.kv.dto.req.BatchFindCommentContentReqDTO;
import com.gchong3068.xiaohongshu.kv.dto.req.DeleteCommentContentReqDTO;

/**
 * @Description: 评论内容储存
 * @Author: gchong3068
 * @Date: 2025/1/10 15:26
 * @Version: v1.0.0
 **/
public interface CommentContentService {

    /**
     * 批量添加评论内容
     * @Auther: gchong3068
     * @Date: 2025/1/10 15:27
 	 * @param batchAddCommentContentReqDTO 批量评论添加
 	 * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> batchAddCommentContent(BatchAddCommentContentReqDTO batchAddCommentContentReqDTO);


    /**
     * 批量查询评论内容
     * @Auther: gchong3068
     * @Date: 2025/2/15 10:34
     * @param batchFindCommentContentReqDTO 批量查询评论
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> batchFindCommentContent(BatchFindCommentContentReqDTO batchFindCommentContentReqDTO);

    /**
     * 删除评论内容
     * @Auther: gchong3068
     * @Date: 2025/6/2 21:06
     * @param deleteCommentContentReqDTO
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> deleteCommentContent(DeleteCommentContentReqDTO deleteCommentContentReqDTO);

}
