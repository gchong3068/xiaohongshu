package com.gchong3068.xiaohongshu.search.biz.controller;

import com.gchong3068.framework.biz.operationlog.aspect.ApiOperationLog;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.search.biz.model.vo.SearchUserReqVO;
import com.gchong3068.xiaohongshu.search.biz.service.UserService;
import com.gchong3068.xiaohongshu.search.dto.RebuildUserDocumentReqDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 用户控制器
 * @Author: gchong3068
 * @Date: 2024/12/4 16:34
 * @Version: v1.0.0
 **/
@RestController
@RequestMapping("/search")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;


    @PostMapping("/user")
    @ApiOperationLog(description = "搜索用户")
    public Response<?> searchUser(@RequestBody @Validated SearchUserReqVO searchUserReqVO){
        return userService.searchUser(searchUserReqVO);
    }


    // ===================================== 对其他服务提供的接口 =====================================
    @PostMapping("/user/document/rebuild")
    @ApiOperationLog(description = "用户文档重建")
    public Response<Long> rebuildDocument(@Validated @RequestBody RebuildUserDocumentReqDTO rebuildUserDocumentReqDTO) {
        return userService.rebuildDocument(rebuildUserDocumentReqDTO);
    }
}
