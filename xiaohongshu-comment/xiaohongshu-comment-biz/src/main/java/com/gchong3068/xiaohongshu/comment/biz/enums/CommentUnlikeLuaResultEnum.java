package com.gchong3068.xiaohongshu.comment.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 评论取消点赞：执行 Lua 脚本返回结果
 * @Author: gchong3068
 * @Date: 2025/5/9 17:54
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum CommentUnlikeLuaResultEnum {

    // 布隆过滤器不存在
    NOT_EXIST(-1L),
    // 评论已点赞
    COMMENT_LIKED(1L),
    // 评论未点赞
    COMMENT_NOT_LIKED(0L),
    ;


    private final Long code;

    /**
     * 根据类型 code 获取对应枚举
     * @param code
     * @return com.gchong3068.xiaohongshu.comment.biz.enums.CommentUnlikeLuaResultEnum
     * @Auther: gchong3068
     * @Date: 2025/5/9 17:56
     **/
    public static CommentUnlikeLuaResultEnum valueOf(Long code) {
        for (CommentUnlikeLuaResultEnum commentUnlikeLuaResultEnum : CommentUnlikeLuaResultEnum.values()) {
            if (Objects.equals(code, commentUnlikeLuaResultEnum.getCode())) {
                return commentUnlikeLuaResultEnum;
            }
        }
        return null;
    }
}
