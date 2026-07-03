package com.gchong3068.xiaohongshu.note.biz.controller;

import com.gchong3068.framework.biz.operationlog.aspect.ApiOperationLog;
import com.gchong3068.framework.common.response.PageResponse;
import com.gchong3068.xiaohongshu.note.biz.model.vo.FindDiscoverNotePageListReqVO;
import com.gchong3068.xiaohongshu.note.biz.model.vo.FindDiscoverNoteRspVO;
import com.gchong3068.xiaohongshu.note.biz.service.DiscoverService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 发现页
 * @Author: gchong3068
 * @Date: 2025/6/12 18:04
 * @Version: v1.0.0
 **/
@RestController
@RequestMapping("/discover")
@Slf4j
public class DiscoverController {

    @Resource
    private DiscoverService discoverService;

    @PostMapping(value = "/note/list")
    @ApiOperationLog(description = "发现页-查询笔记列表")
    public PageResponse<FindDiscoverNoteRspVO> findNoteList(@Validated @RequestBody FindDiscoverNotePageListReqVO findDiscoverNoteListReqVO) {
        return discoverService.findNoteList(findDiscoverNoteListReqVO);
    }

}
