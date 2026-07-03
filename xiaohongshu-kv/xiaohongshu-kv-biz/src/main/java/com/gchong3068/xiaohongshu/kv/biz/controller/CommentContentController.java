package com.gchong3068.xiaohongshu.kv.biz.controller;

import com.gchong3068.framework.biz.operationlog.aspect.ApiOperationLog;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.kv.biz.service.CommentContentService;
import com.gchong3068.xiaohongshu.kv.dto.req.BatchAddCommentContentReqDTO;
import com.gchong3068.xiaohongshu.kv.dto.req.BatchFindCommentContentReqDTO;
import com.gchong3068.xiaohongshu.kv.dto.req.DeleteCommentContentReqDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description: 评论Controller
 * @Author: gchong3068
 * @Date: 2025/1/10 15:39
 * @Version: v1.0.0
 **/
@RefreshScope
@RequestMapping("/kv")
@Slf4j
public class CommentContentController {

    @Resource
    private CommentContentService commentContentService;

    @PostMapping("/comment/content/batchAdd")
    @ApiOperationLog(description = "批量储存评论")
    public Response<?> batchAddCommentContent(@Validated @RequestBody BatchAddCommentContentReqDTO batchAddCommentContentReqDTO){
        return commentContentService.batchAddCommentContent(batchAddCommentContentReqDTO);
    }

    @PostMapping("/comment/content/batchFind")
    @ApiOperationLog(description = "批量查询评论内容")
    public Response<?> batchFindCommentContent(@Validated @RequestBody BatchFindCommentContentReqDTO batchFindCommentContentReqDTO){
        return commentContentService.batchFindCommentContent(batchFindCommentContentReqDTO);
    }

    @PostMapping(value = "/comment/content/delete")
    @ApiOperationLog(description = "删除评论内容")
    public Response<?> deleteCommentContent(@Validated @RequestBody DeleteCommentContentReqDTO deleteCommentContentReqDTO){
        return commentContentService.deleteCommentContent(deleteCommentContentReqDTO);
    }

}
