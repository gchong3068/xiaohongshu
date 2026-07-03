package com.gchong3068.xiaohongshu.count.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 评论级别枚举
 * @Author: gchong3068
 * @Date: 2025/1/12 16:05
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum CommentLevelEnum {

    ONE(1),
    TWO(2),
    ;

    private final Integer code;

}
