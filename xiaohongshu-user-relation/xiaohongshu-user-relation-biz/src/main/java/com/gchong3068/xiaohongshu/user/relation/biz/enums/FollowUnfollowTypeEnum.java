package com.gchong3068.xiaohongshu.user.relation.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 关注、取关类型枚举
 * @Author: gchong3068
 * @Date: 2024/10/8 21:22
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum FollowUnfollowTypeEnum {

    FOLLOW(1), //关注
    UNFOLLOW(0), //取关
    ;

    private final Integer code;
}
