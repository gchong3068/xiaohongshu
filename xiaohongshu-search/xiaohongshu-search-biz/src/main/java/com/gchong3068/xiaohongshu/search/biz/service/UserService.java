package com.gchong3068.xiaohongshu.search.biz.service;

import com.gchong3068.framework.common.response.PageResponse;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.search.biz.model.vo.SearchUserReqVO;
import com.gchong3068.xiaohongshu.search.biz.model.vo.SearchUserRspVO;
import com.gchong3068.xiaohongshu.search.dto.RebuildUserDocumentReqDTO;

/**
 * @Description: 用户搜索业务
 * @Author: gchong3068
 * @Date: 2024/12/4 16:16
 * @Version: v1.0.0
 **/
public interface UserService {

    /**
     * 搜索用户
     * @Auther: gchong3068
     * @Date: 2024/12/4 16:17
     * @param searchUserReqVO 请求参数
     * @return com.gchong3068.framework.common.response.PageResponse<com.gchong3068.xiaohongshu.search.model.vo.SearchUserRspVO>
     **/
    PageResponse<SearchUserRspVO> searchUser(SearchUserReqVO searchUserReqVO);

    /**
     * 重建用户文档
     * @Auther: gchong3068
     * @Date: 2024/12/19 17:05
 	 * @param rebuildUserDocumentReqDTO 重建用户文档ID
 	 * @return com.gchong3068.framework.common.response.Response<java.lang.Long>
     **/
    Response<Long> rebuildDocument(RebuildUserDocumentReqDTO rebuildUserDocumentReqDTO);

}
