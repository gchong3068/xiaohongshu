package com.gchong3068.framework.common.util;

import java.util.regex.Pattern;

/**
 * @Description: 参数校验工具
 * @Author: gchong3068
 * @Date: 2024/7/3 19:10
 * @Version: v1.0.0
 **/
public class ParamUtils {
    private ParamUtils(){

    }

    // ============================== 校验昵称 ==============================
    //昵称长度范围
    private static final int NICK_NAME_MIN_LENGTH = 2;
    private static final int NICK_NAME_MAX_LENGTH = 24;

    private static final String NICK_NAME_REGEX = "[!@#$%^&*(),.?\":{}|<>]";


    /**
     * 昵称校验
     * @Auther: gchong3068
     * @Date: 2024/7/3 19:28
     * @param nickname 昵称
     * @return boolean
     **/
    public static boolean checkNickname(String nickname){
        if(nickname.length() > NICK_NAME_MAX_LENGTH || nickname.length() < NICK_NAME_MIN_LENGTH){
            return false;

        }

        //检查字符
        Pattern pattern = Pattern.compile(NICK_NAME_REGEX);
        return !pattern.matcher(nickname).find();
    }

    // ============================== 校验小红书号 ==============================
    //定义ID长度
    private static final int ID_MIN_LENGTH = 6;
    private static final int ID_MAX_LENGTH = 15;

    private static final String ID_REGEX = "^[a-zA-Z0-9_]+$";

    /**
     * 小红书ID 校验
     * @Auther: gchong3068
     * @Date: 2024/7/3 19:29
     * @param xiaohongshuId Id
     * @return boolean
     **/
    public static boolean checkXiaohongshuId(String xiaohongshuId) {
        // 检查长度
        if (xiaohongshuId.length() < ID_MIN_LENGTH || xiaohongshuId.length() > ID_MAX_LENGTH) {
            return false;
        }
        // 检查格式
        Pattern pattern = Pattern.compile(ID_REGEX);
        return pattern.matcher(xiaohongshuId).matches();
    }

    /**
     * 字符串长度校验
     * @Auther: gchong3068
     * @Date: 2024/7/3 19:29
 	 * @param str 字符串
 	 * @param length 长度
 	 * @return boolean
     **/
    public static boolean checkLength(String str, int length) {
        // 检查长度
        return !str.isEmpty() && str.length() <= length;
    }

}
