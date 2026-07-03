package com.gchong3068.xiaohongshu.gateway.constant;

/**
 * @Description: Redis Key 常量
 * @Author: gchong3068
 * @Date: 2024/6/17 10:45
 * @Version: v1.0.0
 **/
public class RedisKeyConstant {

    /**
     * 用户对应角色集合KEY前缀
     */
    public static final String USER_ROLES_KEY_PREFIX = "user:roles:";

    /**
     * 角色对应权限集合KEY前缀
     */
    public static final String ROLE_PERMISSIONS_KEY_PREFIX = "role:permissions:";


    /**
     * Sa-Token 登录 Token Key 前缀
     */
    public static final String SA_TOKEN_TOKEN_KEY_PREFIX = "Authorization:login:token:";

    /**
     * @Description: 构建角色对应权限集合KEY
     * @Auther: gchong3068
     * @Date: 2024/6/17 10:49
     */
    public static String buildRolePermissionKey(String roleKey){
        return ROLE_PERMISSIONS_KEY_PREFIX + roleKey;
    }

    /**
     * @Description: 构建用户-角色Key
     * @Auther: gchong3068
     * @Date: 2024/6/17 10:50
     */
    public static String buildUserROleKey(Long userId){
        return USER_ROLES_KEY_PREFIX + userId;
    }

}
