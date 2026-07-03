package com.gchong3068.xiaohongshu.note.biz.rpc;

import com.gchong3068.xiaohongshu.distributed.id.generator.api.DistributedIdGeneratorFeignApi;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Description: 用户id生成器rpc服务
 * @Author: gchong3068
 * @Date: 2024/8/17 11:04
 * @Version: v1.0.0
 **/
@Component
public class DistributedIdGeneratorRpcService {
    
    @Resource
    private DistributedIdGeneratorFeignApi distributedIdGeneratorFeignApi;
    
    
    /*** 
     * 生成雪花算法Id
     * @Auther: gchong3068
     * @Date: 2024/8/17 11:06
 	 * @return java.lang.String
     **/
    public String getSnowflakeId() {
        return distributedIdGeneratorFeignApi.getSnowflakeId("test");
    }
}
