package com.gchong3068.xiaohongshu.user.relation.biz.constant;

/**
 * @Description: MQ 常量
 * @Author: gchong3068
 * @Date: 2024/9/13 22:18
 * @Version: v1.0.0
 **/
public interface MQConstants {

    /**
     * Topic: 关注、取关共用一个
     */
    String TOPIC_FOLLOW_OR_UNFOLLOW = "FollowUnfollowTopic";

    /**
     * 关注标签
     */
    String TAG_FOLLOW = "Follow";

    /**
     * 取关标签
     */
    String TAG_UNFOLLOW = "Unfollow";

    /**
     * 粉丝数计数
     */
    String TOPIC_COUNT_FANS = "CountFansTopic";

    /**
     * 关注数计数
     */
    String TOPIC_COUNT_FOLLOWING = "CountFollowingTopic";





}
