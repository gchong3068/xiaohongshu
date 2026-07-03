package com.gchong3068.xiaohongshu.comment.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 评论点赞：执行 Lua 脚本返回结果
 * @Author: gchong3068
 * @Date: 2025/4/23 18:50
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum CommentLikeLuaResultEnum {

    // 布隆过滤器不存在
    NOT_EXIST(-1L),
    // 评论已点赞
    COMMENT_LIKED(1L),
    // 评论点赞成功
    COMMENT_LIKE_SUCCESS(0L),
    ;

    private final Long code;


    /**
     * 根据code 获取对应枚举
     * @Auther: gchong3068
     * @Date: 2025/4/23 18:51
     * @param code
     * @return com.gchong3068.xiaohongshu.comment.biz.enums.CommentLikeLuaResultEnum
     **/
    public static CommentLikeLuaResultEnum valueOf(Long code) {
        for (CommentLikeLuaResultEnum commentLikeLuaResultEnum : CommentLikeLuaResultEnum.values()) {
            if (Objects.equals(code, commentLikeLuaResultEnum.getCode())) {
                return commentLikeLuaResultEnum;
            }
        }
        return null;
    }
}
