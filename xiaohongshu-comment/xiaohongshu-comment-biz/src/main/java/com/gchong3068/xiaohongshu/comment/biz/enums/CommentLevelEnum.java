package com.gchong3068.xiaohongshu.comment.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 评论等级枚举
 * @Author: gchong3068
 * @Date: 2025/1/10 16:39
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum CommentLevelEnum {

    ONE(1),
    TWO(2),
    ;

    private final Integer code;


    /**
     * 根据code 获取对应枚举
     * @Auther: gchong3068
     * @Date: 2025/6/2 21:49
     * @param code
     * @return com.gchong3068.xiaohongshu.comment.biz.enums.CommentLevelEnum
     **/
    public static CommentLevelEnum valueOf(Integer code){
        for (CommentLevelEnum commentLevelEnum : CommentLevelEnum.values()){
            if (Objects.equals(code, commentLevelEnum.getCode())){
                return commentLevelEnum;
            }
        }
        return null;
    }

}
