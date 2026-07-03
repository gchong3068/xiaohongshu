package com.gchong3068.xiaohongshu.count.biz.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.gchong3068.framework.biz.operationlog.aspect.ApiOperationLog;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.count.biz.server.UserCountService;
import com.gchong3068.xiaohongshu.count.dto.FindUserCountByIdReqDTO;
import com.gchong3068.xiaohongshu.count.dto.FindUserCountByIdRspDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 用户维度计数
 * @Author: gchong3068
 * @Date: 2025/6/5 16:41
 * @Version: v1.0.0
 **/
@RestController
@RequestMapping("/count")
@Slf4j
public class UserCountController {

    @Resource
    private UserCountService userCountService;


    @PostMapping(value = "/user/data")
    @ApiOperationLog(description = "获取用户计数数据")
    @SentinelResource(value = "findUserCountData4Controller",blockHandler = "blockHandler4findUserCountData")
    public Response<FindUserCountByIdRspDTO> findUserCountData(@Validated @RequestBody FindUserCountByIdReqDTO findUserCountsByIdReqDTO) {
        return userCountService.findUserCountData(findUserCountsByIdReqDTO);
    }

    /**
     * blockHandler 函数，原方法调用被限流/降级/系统保护的时候调用
     * 注意, 需要包含限流方法的所有参数，和 BlockException 参数
     * @param findUserCountsByIdReqDTO
     * @param blockException
     */
    public Response<FindUserCountByIdRspDTO> blockHandler4findUserCountData(FindUserCountByIdReqDTO findUserCountsByIdReqDTO, BlockException blockException) {
        log.warn("## findUserCountData() 方法被限流: {}", JsonUtils.toJsonString(findUserCountsByIdReqDTO));



        return Response.success(FindUserCountByIdRspDTO.builder()
                .userId(findUserCountsByIdReqDTO.getUserId())
                .collectTotal(0L)
                .fansTotal(0L)
                .followingTotal(0L)
                .likeTotal(0L)
                .noteTotal(0L)
                .build());
    }
}
