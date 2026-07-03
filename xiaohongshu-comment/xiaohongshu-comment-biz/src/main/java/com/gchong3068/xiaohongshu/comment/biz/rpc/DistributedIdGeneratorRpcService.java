package com.gchong3068.xiaohongshu.comment.biz.rpc;

import com.gchong3068.xiaohongshu.distributed.id.generator.api.DistributedIdGeneratorFeignApi;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Description: 分布式ID服务
 * @Author: gchong3068
 * @Date: 2025/1/10 16:15
 * @Version: v1.0.0
 **/
@Component
public class DistributedIdGeneratorRpcService {

    @Resource
    private DistributedIdGeneratorFeignApi distributedIdGeneratorFeignApi;


    /**
     * 生成评论ID
     * @Auther: gchong3068
     * @Date: 2025/1/10 16:18
 	 * @return java.lang.String
     **/
    public String generateCommentId(){
        return distributedIdGeneratorFeignApi.getSegmentId("leaf-segment-comment-id");
    }

}
