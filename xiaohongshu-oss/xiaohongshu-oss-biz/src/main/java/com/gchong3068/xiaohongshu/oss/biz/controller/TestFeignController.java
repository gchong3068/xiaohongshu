package com.gchong3068.xiaohongshu.oss.biz.controller;

import com.gchong3068.framework.biz.operationlog.aspect.ApiOperationLog;
import com.gchong3068.framework.common.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: Feign 测试接口
 * @Author: gchong3068
 * @Date: 2024/7/4 21:49
 * @Version: v1.0.0
 **/
@RestController
@RequestMapping("/file")
@Slf4j
public class TestFeignController {

    @PostMapping(value = "/test")
    @ApiOperationLog(description = "Feign 测试接口")
    public Response<?> test() {
        return Response.success();
    }
}
