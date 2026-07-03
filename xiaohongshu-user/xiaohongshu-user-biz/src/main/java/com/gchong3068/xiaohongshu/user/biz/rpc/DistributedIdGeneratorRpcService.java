package com.gchong3068.xiaohongshu.user.biz.rpc;

import com.gchong3068.xiaohongshu.distributed.id.generator.api.DistributedIdGeneratorFeignApi;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;


/**
 * @Description: ID生成器RPC服务
 * @Author: gchong3068
 * @Date: 2024/8/9 22:05
 * @Version: v1.0.0
 **/
@Component
public class DistributedIdGeneratorRpcService {

    @Resource
    private DistributedIdGeneratorFeignApi distributedIdGeneratorFeignApi;


    /**
     * Leaf 号段模式获取ID
     */
    private static final String BIZ_TAG_XIAOHONGSHU_ID = "leaf-segment-xiaohongshu-id";

    /**
     * Leaf 号段模式用户ID标识
     */
    private static final String BIZ_TAG_USER_ID = "leaf-segment-user-id";

    /**
     * 调用分布式ID生成服务
     * @Auther: gchong3068
     * @Date: 2024/8/9 22:09
 	 * @return java.lang.String
     **/
    public String getXiaohongshuId(){
        return distributedIdGeneratorFeignApi.getSegmentId(BIZ_TAG_XIAOHONGSHU_ID);
    }
    
    
    /**
     * 调用ID生成服务，生成用户ID
     * @Auther: gchong3068
     * @Date: 2024/8/9 22:28
 	 * @return java.lang.String
     **/
    public String getUserId(){
        return distributedIdGeneratorFeignApi.getSegmentId(BIZ_TAG_USER_ID);
    }
}
