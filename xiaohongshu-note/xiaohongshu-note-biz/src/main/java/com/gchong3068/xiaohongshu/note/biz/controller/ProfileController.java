package com.gchong3068.xiaohongshu.note.biz.controller;

import com.gchong3068.framework.biz.operationlog.aspect.ApiOperationLog;
import com.gchong3068.framework.common.response.PageResponse;
import com.gchong3068.xiaohongshu.note.biz.model.vo.FindProfileNotePageListReqVO;
import com.gchong3068.xiaohongshu.note.biz.model.vo.FindProfileNoteRspVO;
import com.gchong3068.xiaohongshu.note.biz.service.ProfileService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 个人主页
 * @Author: gchong3068
 * @Date: 2025/6/12 18:04
 * @Version: v1.0.0
 **/
@RestController
@RequestMapping("/profile")
@Slf4j
public class ProfileController {


    @Resource
    private ProfileService profileService;

    @PostMapping(value = "/note/list")
    @ApiOperationLog(description = "个人主页-查询笔记列表")
    public PageResponse<FindProfileNoteRspVO> findNoteList(@Validated @RequestBody FindProfileNotePageListReqVO findProfileNotePageListReqVO) {
        return profileService.findNoteList(findProfileNotePageListReqVO);
    }

}
