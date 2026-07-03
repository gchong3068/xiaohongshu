package com.gchong3068.xiaohongshu.kv.biz.controller;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.kv.dto.req.AddNoteContentReqDTO;
import com.gchong3068.xiaohongshu.kv.biz.service.NoteContentService;
import com.gchong3068.xiaohongshu.kv.dto.req.DeleteNoteContentReqDTO;
import com.gchong3068.xiaohongshu.kv.dto.req.FindNoteContentReqDTO;
import com.gchong3068.xiaohongshu.kv.dto.rsp.FindNoteContentRspDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 笔记Controller
 * @Author: gchong3068
 * @Date: 2024/7/28 22:21
 * @Version: v1.0.0
 **/
@RestController
@RequestMapping("/kv")
@Slf4j
public class NoteContentController {
    @Resource
    private NoteContentService noteContentService;

    /**
     * 添加笔记
     * @Auther: gchong3068
     * @Date: 2024/7/31 21:20
     * @param addNoteContentReqDTO 笔记内容
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @PostMapping("/note/content/add")
    public Response<?> addNoteContent(@Validated @RequestBody AddNoteContentReqDTO addNoteContentReqDTO){
        return noteContentService.addNoteContent(addNoteContentReqDTO);
    }


    /**
     * 查询笔记
     * @Auther: gchong3068
     * @Date: 2024/7/31 21:20
     * @param findNoteContentReqDTO 笔记ID
     * @return com.gchong3068.framework.common.response.Response<com.gchong3068.xiaohongshu.kv.dto.rsp.FindNoteContentRspDTO>
     **/
    @PostMapping("/note/content/find")
    public Response<FindNoteContentRspDTO> findNOteContent(@Validated @RequestBody FindNoteContentReqDTO findNoteContentReqDTO){
        return noteContentService.findNoteContent(findNoteContentReqDTO);
    }


    /**
     * 删除笔记
     * @Auther: gchong3068
     * @Date: 2024/7/31 21:21
     * @param deleteNoteContentReqDTO 笔记ID
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @PostMapping("/note/content/delete")
    public Response<?> deleteNoteContent(@Validated @RequestBody DeleteNoteContentReqDTO deleteNoteContentReqDTO){
        return noteContentService.deleteNoteContent(deleteNoteContentReqDTO);
    }
}
