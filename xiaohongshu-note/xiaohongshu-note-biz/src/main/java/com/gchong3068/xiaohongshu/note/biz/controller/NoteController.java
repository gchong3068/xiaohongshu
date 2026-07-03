package com.gchong3068.xiaohongshu.note.biz.controller;

import com.gchong3068.framework.biz.operationlog.aspect.ApiOperationLog;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.note.biz.model.vo.*;
import com.gchong3068.xiaohongshu.note.biz.service.NoteService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 笔记
 * @Author: gchong3068
 * @Date: 2024/8/17 11:33
 * @Version: v1.0.0
 **/
@RestController
@RequestMapping("/note")
@Slf4j
public class NoteController {

    @Resource
    private NoteService noteService;

    @PostMapping(value = "/publish")
    @ApiOperationLog(description = "笔记发布")
    public Response<?> publishNote(@Validated @RequestBody PublishNoteReqVO publishNoteReqVO){
        return noteService.publishNote(publishNoteReqVO);
    }

    @PostMapping("/detail")
    @ApiOperationLog(description = "笔记详情")
    public Response<FindNoteDetailRspVO> findNoteDetail(@Validated @RequestBody FindNoteDetailReqVO findNoteDetailReqVO){
        return noteService.findNoteDetail(findNoteDetailReqVO);
    }

    @PostMapping(value = "/update")
    @ApiOperationLog(description = "笔记修改")
    public Response<?> updateNote(@Validated @RequestBody UpdateNoteReqVO updateNoteReqVO){
        return noteService.updateNote(updateNoteReqVO);
    }

    @PostMapping("/delete")
    @ApiOperationLog(description = "删除笔记")
    public Response<?> deleteNote(@Validated @RequestBody DeleteNoteReqVO deleteNoteReqVO){
        return noteService.deleteNote(deleteNoteReqVO);
    }

    @PostMapping("/visible/onlyme")
    @ApiOperationLog(description = "笔记仅自己可见")
    public Response<?> visibleOnlyMe(@Validated @RequestBody UpdateNoteVisibleOnlyMeReqVO updateNoteVisibleOnlyMeReqVO){
        return noteService.visibleOnlyMe(updateNoteVisibleOnlyMeReqVO);
    }

    @PostMapping("/top")
    @ApiOperationLog(description = "笔记置顶")
    public Response<?> topNote(@Validated @RequestBody TopNoteReqVO topNoteReqVO){
        return noteService.topNote(topNoteReqVO);
    }

    @PostMapping("/like")
    @ApiOperationLog(description = "点赞笔记")
    public Response<?> likeNote(@Validated @RequestBody LikeNoteReqVO likeNoteReqVO){
        return noteService.likeNote(likeNoteReqVO);
    }

    @PostMapping(value = "/unlike")
    @ApiOperationLog(description = "取消点赞笔记")
    public Response<?> unlikeNote(@Validated @RequestBody UnlikeNoteReqVO unlikeNoteReqVO) {
        return noteService.unlikeNote(unlikeNoteReqVO);
    }


    @PostMapping(value = "/collect")
    @ApiOperationLog(description = "收藏笔记")
    public Response<?> collectNote(@Validated @RequestBody CollectNoteReqVO collectNoteReqVO) {
        return noteService.collectNote(collectNoteReqVO);
    }

    @PostMapping(value = "/uncollect")
    @ApiOperationLog(description = "取消收藏笔记")
    public Response<?> unCollectNote(@Validated @RequestBody UnCollectNoteReqVO unCollectNoteReqVO){
        return noteService.unCollectNote(unCollectNoteReqVO);
    }

    @PostMapping(value = "/isLikedAndCollectedData")
    @ApiOperationLog(description = "获取当前用户是否点赞、收藏数据")
    public Response<FindNoteIsLikedAndCollectedRspVO> isLikedAndCollectedData(@Validated @RequestBody FindNoteISLikedAndCollectedReqVO findNoteIsLikedAndCollectedReqVO) {
        return noteService.isLikedAndCollectedData(findNoteIsLikedAndCollectedReqVO);
    }

    @PostMapping(value = "/published/list")
    @ApiOperationLog(description = "用户主页 - 已发布笔记列表")
    public Response<FindPublishedNoteListRspVO> findPublishedNoteList(@Validated @RequestBody FindPublishedNoteListReqVO findPublishedNoteListReqVO){
        return noteService.findPublishedNoteList(findPublishedNoteListReqVO);
    }
}
