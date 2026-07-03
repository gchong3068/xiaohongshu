package com.gchong3068.xiaohongshu.comment.biz.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Description: 热度值计算
 * @Author: gchong3068
 * @Date: 2025/2/7 20:34
 * @Version: v1.0.0
 **/
public class HeatCalculator {

    private static final double LIKE_WEIGHT = 0.7;  // 点赞权重 70%
    private static final double REPLY_WEIGHT = 0.3; // 回复权重 30%

    public static BigDecimal calculateHeat (long likeCount , long replyCount) {
        BigDecimal likeWeight = new BigDecimal(LIKE_WEIGHT);
        BigDecimal replyWeight = new BigDecimal(REPLY_WEIGHT);

        //转换点赞数和回复数
        BigDecimal likeCountBD = new BigDecimal(likeCount);
        BigDecimal replyCountBD = new BigDecimal(replyCount);

        //计算热度
        BigDecimal heat = likeCountBD.multiply(likeWeight).add(replyCountBD.multiply(replyWeight));

        //保留两位小数
        return heat.setScale(2, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        int likeCount = 150;
        int replyCount = 10;

        BigDecimal heat = calculateHeat(likeCount , replyCount);

        System.out.println("heat = " + heat);
    }

}
