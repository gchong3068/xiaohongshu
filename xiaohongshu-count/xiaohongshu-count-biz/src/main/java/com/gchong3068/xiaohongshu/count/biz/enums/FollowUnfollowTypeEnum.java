package com.gchong3068.xiaohongshu.count.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 关注/取关类型枚举
 * @Author: gchong3068
 * @Date: 2024/10/10 18:49
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum FollowUnfollowTypeEnum{

    FOLLOW(1),

    UNFOLLOW(0),
    ;

    private final Integer code;

    public static FollowUnfollowTypeEnum valueOf(Integer code) {
        for (FollowUnfollowTypeEnum followUnfollowTypeEnum : FollowUnfollowTypeEnum.values()) {
            if (Objects.equals(code, followUnfollowTypeEnum.getCode())) {
                return followUnfollowTypeEnum;
            }
        }
        return null;
    }

}
