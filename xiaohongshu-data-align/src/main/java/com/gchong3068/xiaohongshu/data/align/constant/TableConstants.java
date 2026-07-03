package com.gchong3068.xiaohongshu.data.align.constant;

/**
 * @Description: 表常量类
 * @Author: gchong3068
 * @Date: 2024/11/8 15:17
 * @Version: v1.0.0
 **/
public class TableConstants {

    /**
     * 表名中分隔符
     */
    private static final String TABLE_NAME_SEPARATE = "_";

    /**
     * 拼接表名后缀
     * @Auther: gchong3068
     * @Date: 2024/11/8 15:19 
 	 * @param date 时间
 	 * @param hashKey 哈希值
 	 * @return java.lang.String
     **/
    public static String buildTableNameSuffix(String date , long hashKey){
        return date + TABLE_NAME_SEPARATE + hashKey;
    }

}
