package com.gchong3068.xiaohongshu.data.align.rpc;

import com.gchong3068.xiaohongshu.search.api.SearchFeignApi;
import com.gchong3068.xiaohongshu.search.dto.RebuildNoteDocumentReqDTO;
import com.gchong3068.xiaohongshu.search.dto.RebuildUserDocumentReqDTO;
import org.springframework.stereotype.Component;

/**
 * @Description: 搜索服务RPC
 * @Author: gchong3068
 * @Date: 2024/12/19 17:32
 * @Version: v1.0.0
 **/
@Component
public class SearchRpcService {

    private SearchFeignApi searchFeignApi;


    /**
     * 重建笔记文档接口
     * @Auther: gchong3068
     * @Date: 2024/12/19 17:35
 	 * @param noteId 笔记ID
     **/
    public void rebuildNoteDocument(Long noteId){
        RebuildNoteDocumentReqDTO rebuildNoteDocumentReqDTO  = RebuildNoteDocumentReqDTO.builder()
                .id(noteId)
                .build();

        searchFeignApi.rebuildNoteDocument(rebuildNoteDocumentReqDTO);
    }

    /**
     * 重建用户文档接口
     * @Auther: gchong3068
     * @Date: 2024/12/19 17:36
 	 * @param userId 用户ID
     **/
    public void rebuildUserDocument(Long userId) {
        RebuildUserDocumentReqDTO rebuildUserDocumentReqDTO = RebuildUserDocumentReqDTO.builder()
                .id(userId)
                .build();

        searchFeignApi.rebuildUserDocument(rebuildUserDocumentReqDTO);
    }


}
