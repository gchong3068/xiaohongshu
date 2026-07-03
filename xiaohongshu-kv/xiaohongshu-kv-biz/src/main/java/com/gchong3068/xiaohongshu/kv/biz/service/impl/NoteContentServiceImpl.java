package com.gchong3068.xiaohongshu.kv.biz.service.impl;

import com.gchong3068.framework.common.exception.BizException;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.kv.biz.domain.dataobject.NoteContentDO;
import com.gchong3068.xiaohongshu.kv.biz.domain.repository.NoteContentRepository;
import com.gchong3068.xiaohongshu.kv.biz.enums.ResponseCodeEnum;
import com.gchong3068.xiaohongshu.kv.dto.req.AddNoteContentReqDTO;
import com.gchong3068.xiaohongshu.kv.biz.service.NoteContentService;
import com.gchong3068.xiaohongshu.kv.dto.req.DeleteNoteContentReqDTO;
import com.gchong3068.xiaohongshu.kv.dto.req.FindNoteContentReqDTO;
import com.gchong3068.xiaohongshu.kv.dto.rsp.FindNoteContentRspDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * @Description: 笔记内容服务实现
 * @Author: gchong3068
 * @Date: 2024/7/28 22:14
 * @Version: v1.0.0
 **/
@Service
@Slf4j
public class NoteContentServiceImpl implements NoteContentService {

    @Resource
    private NoteContentRepository noteContentRepository;

    @Override
    public Response<?> addNoteContent(AddNoteContentReqDTO addNoteContentReqDTO) {
        // 笔记 ID
        String uuid = addNoteContentReqDTO.getUuid();
        //内容
        String content = addNoteContentReqDTO.getContent();

        NoteContentDO noteContentDO = NoteContentDO.builder()
                .id(UUID.fromString(uuid))
                .content(content)
                .build();

        noteContentRepository.save(noteContentDO);
        return Response.success();
    }




    @Override
    public Response<FindNoteContentRspDTO> findNoteContent(FindNoteContentReqDTO findNoteContentReqDTO) {
        String uuid = findNoteContentReqDTO.getUuid();

        Optional<NoteContentDO> optional = noteContentRepository.findById(UUID.fromString(uuid));
        if (optional.isEmpty()){
            throw new BizException(ResponseCodeEnum.NOTE_CONTENT_NOT_FOUND);
        }

        NoteContentDO noteContentDO = optional.get();

        FindNoteContentRspDTO findNoteContentRspDTO = FindNoteContentRspDTO.builder()
                .uuid(noteContentDO.getId())
                .content(noteContentDO.getContent())
                .build();

        return Response.success(findNoteContentRspDTO);
    }

    /**
     * 删除笔记内容
     * @Auther: gchong3068
     * @Date: 2024/8/15 22:25
 	 * @param deleteNoteContentReqDTO 删除笔记内容请求参数
 	 * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @Override
    public Response<?> deleteNoteContent(DeleteNoteContentReqDTO deleteNoteContentReqDTO) {
        String uuid = deleteNoteContentReqDTO.getUuid();

        noteContentRepository.deleteById(UUID.fromString(uuid));
        return Response.success();
    }
}
