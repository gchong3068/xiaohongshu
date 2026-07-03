package com.gchong3068.xiaohongshu.comment.biz.rpc;

import cn.hutool.core.collection.CollUtil;
import com.gchong3068.framework.common.constant.DateConstants;
import com.gchong3068.framework.common.exception.BizException;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.comment.biz.model.bo.CommentBO;
import com.gchong3068.xiaohongshu.kv.api.KeyValueFeignApi;
import com.gchong3068.xiaohongshu.kv.dto.req.*;
import com.gchong3068.xiaohongshu.kv.dto.rsp.FindCommentContentRspDTO;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @Description: KV 键值服务
 * @Author: gchong3068
 * @Date: 2025/1/10 16:55
 * @Version: v1.0.0
 **/
@Component
public class KeyValueRpcService {

    @Resource
    private KeyValueFeignApi keyValueFeignApi;


    /**
     * 批量保存评论内容
     * @Auther: gchong3068
     * @Date: 2025/6/2 21:25
 	 * @param commentBOS
 	 * @return boolean
     **/
    public boolean batchSaveCommentContent(List<CommentBO> commentBOS){
        List<CommentContentReqDTO> comments = Lists.newArrayList();

        // BO --> DTO
        commentBOS.forEach(commentBO -> {
            CommentContentReqDTO commentContentReqDTO = CommentContentReqDTO.builder()
                    .noteId(commentBO.getNoteId())
                    .content(commentBO.getContent())
                    .contentId(commentBO.getContentUuid())
                    .yearMonth(commentBO.getCreateTime().format(DateConstants.DATE_FORMAT_Y_M))
                    .build();
            comments.add(commentContentReqDTO);
        });

        //构建接口入参
        BatchAddCommentContentReqDTO batchAddCommentContentReqDTO = BatchAddCommentContentReqDTO.builder()
                .comments(comments)
                .build();

        //调用KV
        Response<?> response = keyValueFeignApi.batchAddCommentContent(batchAddCommentContentReqDTO);

        if (!response.isSuccess()){
            throw new RuntimeException("批量保存评论内容失败");
        }
        return true;
    }

    /**
     * 批量查询评论内容
     * @Auther: gchong3068
     * @Date: 2025/4/1 16:26
     * @param noteId
     * @param findCommentContentReqDTOS
     * @return java.util.List<com.gchong3068.xiaohongshu.kv.dto.rsp.FindCommentContentRspDTO>
     **/
    public List<FindCommentContentRspDTO> batchFindCommentContent(Long noteId, List<FindCommentContentReqDTO> findCommentContentReqDTOS){
        BatchFindCommentContentReqDTO batchFindCommentContentReqDTO = BatchFindCommentContentReqDTO.builder()
                .noteId(noteId)
                .commentContentKeys(findCommentContentReqDTOS)
                .build();

        Response<List<FindCommentContentRspDTO>> response = keyValueFeignApi.batchFindCommentContent(batchFindCommentContentReqDTO);

        if (!response.isSuccess() || Objects.isNull(response.getData()) || CollUtil.isEmpty(response.getData())){
            return null;
        }
        return response.getData();
    }

    /**
     * 删除评论内容
     * @Auther: gchong3068
     * @Date: 2025/6/2 21:26
     * @param noteId
     * @param createTime
     * @param contentId
     * @return boolean
     **/
    public boolean deleteCommentContent(Long noteId, LocalDateTime createTime, String contentId){
        DeleteCommentContentReqDTO deleteCommentContentReqDTO = DeleteCommentContentReqDTO.builder()
                .noteId(noteId)
                .yearMonth(DateConstants.DATE_FORMAT_Y_M.format(createTime))
                .contentId(contentId)
                .build();

        //调用KV
        Response<?> response = keyValueFeignApi.deleteCommentContent(deleteCommentContentReqDTO);

        if (!response.isSuccess()){
            throw new RuntimeException("删除评论内容失败");
        }

        return true;
    }
}
