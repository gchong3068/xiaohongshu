package com.gchong3068.xiaohongshu.note.biz.controller;

import com.gchong3068.framework.biz.operationlog.aspect.ApiOperationLog;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.note.biz.model.vo.FindTopicListReqVO;
import com.gchong3068.xiaohongshu.note.biz.model.vo.FindTopicRspVO;
import com.gchong3068.xiaohongshu.note.biz.service.TopicService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: 话题
 * @Author: gchong3068
 * @Date: 2025/6/12 18:04
 * @Version: v1.0.0
 **/
@RestController
@RequestMapping("/topic")
@Slf4j
public class TopicController {

    @Resource
    private TopicService topicService;

    @PostMapping(value = "/list")
    @ApiOperationLog(description = "模糊查询话题列表")
    public Response<List<FindTopicRspVO>> findTopicList(@Validated @RequestBody FindTopicListReqVO findTopicListReqVO) {
        return topicService.findTopicList(findTopicListReqVO);
    }
}
