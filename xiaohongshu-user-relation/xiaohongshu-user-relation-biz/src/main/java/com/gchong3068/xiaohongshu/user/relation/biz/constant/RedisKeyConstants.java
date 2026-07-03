package com.gchong3068.xiaohongshu.user.relation.biz.constant;

/**
 * @Description: Redis Key常量
 * @Author: gchong3068
 * @Date: 2024/9/13 9:00
 * @Version: v1.0.0
 **/
public class RedisKeyConstants {

    /*
     *关注列表前缀KEY
     */
    private static final String USER_FOLLOWING_KEY_PREFIX = "following:";

    /*
     * 粉丝列表 KEY 前缀
     */
    private static final String USER_FANS_KEY_PREFIX = "fans:";

    /**
     * 构建关注列表KEY
     * @Auther: gchong3068
     * @Date: 2024/9/13 9:02
 	 * @param userId 用户ID
 	 * @return java.lang.String
     **/
    public static String buildUserFollowingKey(Long userId){
        return USER_FOLLOWING_KEY_PREFIX + userId;
    }

    /**
     * 构建粉丝列表完整的 KEY
     * @param userId 用户ID
     * @return java.lang.String
     */
    public static String buildUserFansKey(Long userId) {
        return USER_FANS_KEY_PREFIX + userId;
    }

}
