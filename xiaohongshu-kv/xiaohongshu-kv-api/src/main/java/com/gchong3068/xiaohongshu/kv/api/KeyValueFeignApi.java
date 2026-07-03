package com.gchong3068.xiaohongshu.kv.api;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.kv.constant.ApiConstants;
import com.gchong3068.xiaohongshu.kv.dto.req.*;
import com.gchong3068.xiaohongshu.kv.dto.rsp.FindCommentContentRspDTO;
import com.gchong3068.xiaohongshu.kv.dto.rsp.FindNoteContentRspDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Description: K-V 键值存储 Feign Api
 * @Author: gchong3068
 * @Date: 2024/7/28 22:28
 * @Version: v1.0.0
 **/
@FeignClient(name = ApiConstants.SERVICE_NAME)
public interface KeyValueFeignApi {

    String PREFIX = "/kv";

    @PostMapping(value = PREFIX + "/note/content/add")
    Response<?> addNoteContent(@RequestBody AddNoteContentReqDTO addNoteContentReqDTO);

    @PostMapping(value = PREFIX + "/note/content/find")
    Response<FindNoteContentRspDTO> findNoteContent(@RequestBody FindNoteContentReqDTO findNoteContentReqDTO);

    @PostMapping(value = PREFIX + "/note/content/delete")
    Response<?> deleteNoteContent(@RequestBody DeleteNoteContentReqDTO deleteNoteContentReqDTO);

    @PostMapping(value = PREFIX + "/comment/content/batchAdd")
    Response<?> batchAddCommentContent(@RequestBody BatchAddCommentContentReqDTO batchAddCommentContentReqDTO);

    @PostMapping(value = PREFIX + "/comment/content/batchFind")
    Response<List<FindCommentContentRspDTO>> batchFindCommentContent(@RequestBody BatchFindCommentContentReqDTO batchFindCommentContentReqDTO);

    @PostMapping(value =  PREFIX + "/comment/content/delete")
    Response<?> deleteCommentContent(@RequestBody DeleteCommentContentReqDTO deleteCommentContentReqDTO);


}
