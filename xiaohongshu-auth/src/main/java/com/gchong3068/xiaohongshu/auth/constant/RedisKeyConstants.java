package com.gchong3068.xiaohongshu.auth.constant;

/**
 * @author gc
 * @date 2024年05月22日 21:11
 */

public class RedisKeyConstants {

    /**
     * 验证码前缀
     */
    private static final String VERIFICATION_CODE_KEY_PREFIX  = "verification_code:";


    /**
     * 构建验证码 KEY
     * @author gc
     * @date 2024/5/31 20:40
     * @return java.lang.String
     */
    public static String buildVerificationCodeKey(String phone) {
        return VERIFICATION_CODE_KEY_PREFIX + phone;
    }



}
