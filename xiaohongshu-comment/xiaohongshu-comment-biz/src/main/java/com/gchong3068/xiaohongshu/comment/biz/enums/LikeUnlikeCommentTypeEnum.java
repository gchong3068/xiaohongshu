package com.gchong3068.xiaohongshu.comment.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 评论点赞、取消点赞 Type
 * @Author: gchong3068
 * @Date: 2025/4/23 18:50
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum LikeUnlikeCommentTypeEnum {

    // 点赞
    LIKE(1),
    // 取消点赞
    UNLIKE(0),
    ;

    private final Integer code;
}
