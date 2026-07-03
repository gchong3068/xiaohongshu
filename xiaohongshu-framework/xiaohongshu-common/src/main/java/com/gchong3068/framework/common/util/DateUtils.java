package com.gchong3068.framework.common.util;

import com.gchong3068.framework.common.constant.DateConstants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * @Description: 时间戳工具类
 * @Author: gchong3068
 * @Date: 2024/9/13 9:04
 * @Version: v1.0.0
 **/
public class DateUtils {

    /**
     * LocalDateTime 转 时间戳
     * @Auther: gchong3068
     * @Date: 2024/9/13 9:06
 	 * @param localDateTime LocalDateTime
 	 * @return long
     **/
    public static long localDateTime2Timestamp(LocalDateTime localDateTime){
        return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }


    /**
     * LocalDateTime 转String
     * @Auther: gchong3068
     * @Date: 2024/12/7 22:28
     * @param time 时间
     * @return java.lang.String
     **/
    public static String localDateTime2String(LocalDateTime time){
        return time.format(DateConstants.DATE_FORMAT_Y_M_D_H_M_S);
    }

    /**
     * LocalDateTime 明显显示
     * @Auther: gchong3068
     * @Date: 2024/12/7 22:41
     * @param dateTime 时间
     * @return java.lang.String
     **/
    public static String formatRelativeTime(LocalDateTime dateTime){
        LocalDateTime now = LocalDateTime.now();

        //计算差距
        long dayDiff = ChronoUnit.DAYS.between(dateTime, now);
        long hoursDiff = ChronoUnit.HOURS.between(dateTime, now);
        long minuteDiff = ChronoUnit.MINUTES.between(dateTime, now);

        if (dayDiff < 1){
            if (hoursDiff < 1){
                return minuteDiff + "分钟前";
            }else {
                return hoursDiff + "小时前";
            }
        }else if (dayDiff == 1){
            return "昨天" + dateTime.format(DateConstants.DATE_FORMAT_H_M);
        }else if (dayDiff > 7){
            return  dayDiff + "天前";
        }else if (dateTime.getYear() == now.getYear()){
            return dateTime.format(DateConstants.DATE_FORMAT_M_D);
        }else {
            return dateTime.format(DateConstants.DATE_FORMAT_Y_M_D);
        }
    }

    /**
     * 计算年龄
     * @Auther: gchong3068
     * @Date: 2025/6/5 17:08
     * @param birthDate 生日
     * @return int
     **/
    public static int calculateAge(LocalDate birthDate){
        LocalDate currentDate = LocalDate.now();

        Period period = Period.between(birthDate, currentDate);

        return period.getYears();
    }

    public static void main(String[] args) {
        // 测试示例
        LocalDateTime dateTime1 = LocalDateTime.now().minusMinutes(10); // 10分钟前
        LocalDateTime dateTime2 = LocalDateTime.now().minusHours(3); // 3小时前
        LocalDateTime dateTime3 = LocalDateTime.now().minusDays(1).minusHours(5); // 昨天 20:12
        LocalDateTime dateTime4 = LocalDateTime.now().minusDays(2); // 2天前
        LocalDateTime dateTime5 = LocalDateTime.now().minusDays(10); // 11-06
        LocalDateTime dateTime6 = LocalDateTime.of(2023, 12, 1, 12, 30, 0); // 2023-12-01

        System.out.println(formatRelativeTime(dateTime1)); // 输出 "10分钟前"
        System.out.println(formatRelativeTime(dateTime2)); // 输出 "3小时前"
        System.out.println(formatRelativeTime(dateTime3)); // 输出 "昨天 20:12"
        System.out.println(formatRelativeTime(dateTime4)); // 输出 "2天前"
        System.out.println(formatRelativeTime(dateTime5)); // 输出 "11-06"
        System.out.println(formatRelativeTime(dateTime6)); // 输出 "2023-12-01"
    }
}
