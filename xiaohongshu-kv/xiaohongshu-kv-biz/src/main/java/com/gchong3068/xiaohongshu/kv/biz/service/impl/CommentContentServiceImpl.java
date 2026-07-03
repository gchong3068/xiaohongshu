package com.gchong3068.xiaohongshu.kv.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.kv.biz.domain.dataobject.CommentContentDO;
import com.gchong3068.xiaohongshu.kv.biz.domain.dataobject.CommentContentPrimaryKey;
import com.gchong3068.xiaohongshu.kv.biz.domain.repository.CommentContentRepository;
import com.gchong3068.xiaohongshu.kv.biz.service.CommentContentService;
import com.gchong3068.xiaohongshu.kv.dto.req.*;
import com.gchong3068.xiaohongshu.kv.dto.rsp.FindCommentContentRspDTO;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Description: 评论内容
 * @Author: gchong3068
 * @Date: 2025/1/10 15:26
 * @Version: v1.0.0
 **/

@Service
public class CommentContentServiceImpl implements CommentContentService {

    @Resource
    private CassandraTemplate cassandraTemplate;
    @Resource
    private CommentContentRepository commentContentRepository;


    /**
     * 批量插入评论
     * @Auther: gchong3068
     * @Date: 2025/1/10 15:32
     * @param batchAddCommentContentReqDTO 批量插入评论入参
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @Override
    public Response<?> batchAddCommentContent(BatchAddCommentContentReqDTO batchAddCommentContentReqDTO) {
        List<CommentContentReqDTO> comments = batchAddCommentContentReqDTO.getComments();

        //DTO --> DO
        List<CommentContentDO> contentDOS = comments.stream()
                .map(commentContentReqDTO -> {
                    CommentContentPrimaryKey commentContentPrimaryKey = CommentContentPrimaryKey.builder()
                            .noteId(commentContentReqDTO.getNoteId())
                            .yearMonth(commentContentReqDTO.getYearMonth())
                            .contentId(UUID.fromString(commentContentReqDTO.getContentId()))
                            .build();

                    CommentContentDO commentContentDO = CommentContentDO.builder()
                            .primaryKey(commentContentPrimaryKey)
                            .content(commentContentReqDTO.getContent())
                            .build();

                    return commentContentDO;
                }).toList();
        //批量插入
        cassandraTemplate.batchOps()
                .insert(contentDOS)
                .execute();
        return Response.success();
    }

    /**
     * 批量查询评论内容
     * @Auther: gchong3068
     * @Date: 2025/4/1 15:55
     * @param batchFindCommentContentReqDTO
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @Override
    public Response<?> batchFindCommentContent(BatchFindCommentContentReqDTO batchFindCommentContentReqDTO) {
        //归属笔记Id
        Long noteId = batchFindCommentContentReqDTO.getNoteId();

        //查询评论的发布年月、内容UUID
        List<FindCommentContentReqDTO> commentContentKeys = batchFindCommentContentReqDTO.getCommentContentKeys();

        //过滤年月
        List<String> yearMonths  = commentContentKeys.stream()
                .map(FindCommentContentReqDTO::getYearMonth)
                .distinct()
                .collect(Collectors.toList());

        //过滤评论UUID
        List<UUID> contentIds = commentContentKeys.stream()
                .map(commentContentKey -> UUID.fromString(commentContentKey.getContentId()))
                .distinct()
                .collect(Collectors.toList());

        //批量查询
        List<CommentContentDO> commentContentDOS = commentContentRepository
                .findByPrimaryKeyNoteIdAndPrimaryKeyYearMonthInAndPrimaryKeyContentIdIn(noteId, yearMonths, contentIds);

        //DO -> DTO
        List<FindCommentContentRspDTO> findCommentContentRspDTOS = Lists.newArrayList();
        if (CollUtil.isNotEmpty(commentContentDOS)){
            findCommentContentRspDTOS = commentContentDOS.stream()
                    .map(commentContentDO -> FindCommentContentRspDTO.builder()
                            .contentId(String.valueOf(commentContentDO.getPrimaryKey().getContentId()))
                            .content(commentContentDO.getContent())
                            .build())
                    .toList();
        }
        return Response.success(findCommentContentRspDTOS);
    }


    /**
     * 删除评论内容
     * @Auther: gchong3068
     * @Date: 2025/6/2 21:08
     * @param deleteCommentContentReqDTO
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @Override
    public Response<?> deleteCommentContent(DeleteCommentContentReqDTO deleteCommentContentReqDTO) {
        Long noteId = deleteCommentContentReqDTO.getNoteId();
        String contentId = deleteCommentContentReqDTO.getContentId();
        String yearMonth = deleteCommentContentReqDTO.getYearMonth();

        commentContentRepository.deleteByPrimaryKeyNoteIdAndPrimaryKeyYearMonthAndPrimaryKeyContentId(noteId, yearMonth, UUID.fromString(contentId));

        return Response.success();
    }
}
