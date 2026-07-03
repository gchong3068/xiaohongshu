package com.gchong3068.xiaohongshu.comment.biz.service;

import com.gchong3068.framework.common.response.PageResponse;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.comment.biz.model.vo.*;

/**
 * @Description: 评论业务
 * @Author: gchong3068
 * @Date: 2024/12/23 20:14
 * @Version: v1.0.0
 **/
public interface CommentService {

    /**
     * 发布评论
     * @Auther: gchong3068
     * @Date: 2024/12/23 20:16
     * @param publishCommentReqVO 评论入参
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> publishComment(PublishCommentReqVO publishCommentReqVO);

    /**
     * 评论列表分页查询
     * @Auther: gchong3068
     * @Date: 2025/4/1 16:17 
 	 * @param findCommentPageListReqVO
 	 * @return com.gchong3068.framework.common.response.PageResponse<com.gchong3068.xiaohongshu.comment.biz.model.vo.FindCommentItemRspVO>
     **/
    PageResponse<FindCommentItemRspVO> findCommentPageList(FindCommentPageListReqVO findCommentPageListReqVO);

    /**
     * 二级评论分页查询
     * @Auther: gchong3068
     * @Date: 2025/4/14 18:19
 	 * @param findChildCommentPageListReqVO
 	 * @return com.gchong3068.framework.common.response.PageResponse<com.gchong3068.xiaohongshu.comment.biz.model.vo.FindChildCommentItemRspVO>
     **/
    PageResponse<FindChildCommentItemRspVO> findChildCommentPageList(FindChildCommentPageListReqVO findChildCommentPageListReqVO);

    /**
     * 评论点赞
     * @Auther: gchong3068
     * @Date: 2025/4/23 18:22
     * @param likeCommentReqVO
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> likeComment(LikeCommentReqVO likeCommentReqVO);

    /**
     * 取消评论点赞
     * @Auther: gchong3068
     * @Date: 2025/5/9 17:51
 	 * @param unLikeCommentReqVO
 	 * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> unLikeComment(UnLikeCommentReqVO unLikeCommentReqVO);

    /***
     * 删除评论
     * @Auther: gchong3068
     * @Date: 2025/6/2 21:20
     * @param deleteCommentReqVO
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> deleteComment(DeleteCommentReqVO deleteCommentReqVO);

    /**
     * 删除本地评论缓存
     * @Auther: gchong3068
     * @Date: 2025/6/2 21:43
     * @param commentId
     **/
    void deleteCommentLocalCache(Long commentId);

}
