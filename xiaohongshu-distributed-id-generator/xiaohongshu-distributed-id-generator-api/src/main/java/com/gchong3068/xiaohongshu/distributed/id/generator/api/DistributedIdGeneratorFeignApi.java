package com.gchong3068.xiaohongshu.distributed.id.generator.api;

import com.gchong3068.xiaohongshu.distributed.id.generator.constant.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description: 全局Id生成器Feign接口
 * @Author: gchong3068
 * @Date: 2024/8/9 21:55
 * @Version: v1.0.0
 **/
@FeignClient(name = ApiConstants.SERVICE_NAME)
public interface DistributedIdGeneratorFeignApi {

    String PREFIX = "/id";

    @GetMapping(value = PREFIX + "/segment/get/{key}")
    String getSegmentId(@PathVariable("key") String key);

    @GetMapping(value = PREFIX + "/snowflake/get/{key}")
    String getSnowflakeId(@PathVariable("key") String key);

}
