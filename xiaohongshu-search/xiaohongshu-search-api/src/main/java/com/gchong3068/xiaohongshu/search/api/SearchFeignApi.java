package com.gchong3068.xiaohongshu.search.api;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.search.constant.ApiConstants;
import com.gchong3068.xiaohongshu.search.dto.RebuildNoteDocumentReqDTO;
import com.gchong3068.xiaohongshu.search.dto.RebuildUserDocumentReqDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description: TODO
 * @Author: gchong3068
 * @Date: 2024/12/19 17:24
 * @Version: v1.0.0
 **/
@FeignClient(name = ApiConstants.SERVICE_NAME)
public interface SearchFeignApi {

    String PREFIX = "/search";

    /**
     * 重建笔记文档
     * @Auther: gchong3068
     * @Date: 2024/12/19 17:27
 	 * @param rebuildNoteDocumentReqDTO 重建笔记文档ID
 	 * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @PostMapping(value = PREFIX + "/note/document/rebuild")
    Response<?> rebuildNoteDocument(@RequestBody RebuildNoteDocumentReqDTO rebuildNoteDocumentReqDTO);

    /**
     * 重建用户文档
     * @Auther: gchong3068
     * @Date: 2024/12/19 17:29
     * @param rebuildUserDocumentReqDTO 重建用户文档ID
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @PostMapping(value = PREFIX + "/user/document/rebuild")
    Response<?> rebuildUserDocument(@RequestBody RebuildUserDocumentReqDTO rebuildUserDocumentReqDTO);

}
