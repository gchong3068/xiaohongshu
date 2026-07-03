package com.gchong3068.framework.common.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @Description: 计数工具类
 * @Author: gchong3068
 * @Date: 2024/12/5 13:06
 * @Version: v1.0.0
 **/
public class NumberUtils {


    /**
     * 数字转换字符创
     * @Auther: gchong3068
     * @Date: 2024/12/5 13:11
 	 * @param number 数字
 	 * @return java.lang.String
     **/
    public static String formatNumberString(long number){
        if (number < 10000){
            return String.valueOf(number); //小于一万显示原始数字
        } else if (number >= 10000 && number<= 100000000) {
            double result = number / 10000.0;
            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.DOWN);  //禁止四舍五入
            String formatted = df.format(result);
            return formatted + "万";
        } else {
            return "9999万";
        }
    }


    public static void main(String[] args){
        // 测试
        System.out.println(formatNumberString(1000));
        System.out.println(formatNumberString(11130));
        System.out.println(formatNumberString(26719300));
        System.out.println(formatNumberString(10000000));
        System.out.println(formatNumberString(999999));
        System.out.println(formatNumberString(150000000));
        System.out.println(formatNumberString(99999));
    }

}
