package com.gchong3068.xiaohongshu.search.biz.controller;

import com.gchong3068.framework.biz.operationlog.aspect.ApiOperationLog;
import com.gchong3068.framework.common.response.PageResponse;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.search.biz.model.vo.SearchNoteReqVO;
import com.gchong3068.xiaohongshu.search.biz.model.vo.SearchNoteRspVO;
import com.gchong3068.xiaohongshu.search.biz.service.NoteService;
import com.gchong3068.xiaohongshu.search.dto.RebuildNoteDocumentReqDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 笔记控制器
 * @Author: gchong3068
 * @Date: 2024/12/6 14:07
 * @Version: v1.0.0
 **/
@RestController
@RequestMapping("/search")
@Slf4j
public class NoteController {

    @Resource
    private NoteService noteService;


    @PostMapping("/note")
    @ApiOperationLog(description = "搜索笔记")
    public PageResponse<SearchNoteRspVO> searchNote(@RequestBody @Validated SearchNoteReqVO searchNoteReqVO){
        return noteService.searchNote(searchNoteReqVO);
    }

    // ===================================== 对其他服务提供的接口 =====================================
    @PostMapping("/note/document/rebuild")
    @ApiOperationLog(description = "用户文档重建")
    public Response<Long> rebuildDocument(@Validated @RequestBody RebuildNoteDocumentReqDTO rebuildNoteDocumentReqDTO) {
        return noteService.rebuildDocument(rebuildNoteDocumentReqDTO);
    }





}
