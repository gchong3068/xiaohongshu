package com.gchong3068.xiaohongshu.user.biz.constant;

/**
 * @Description: TODO
 * @Author: gchong3068
 * @Date: 2025/6/10 18:00
 * @Version: v1.0.0
 **/
public interface MQConstants {

    /**
     * Topic 主题：延迟双删 Redis 用户缓存
     */
    String TOPIC_DELAY_DELETE_USER_REDIS_CACHE = "DelayDeleteUserRedisCacheTopic";

}
