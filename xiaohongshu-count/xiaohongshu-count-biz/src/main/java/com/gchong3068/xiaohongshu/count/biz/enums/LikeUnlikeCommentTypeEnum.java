package com.gchong3068.xiaohongshu.count.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 评论点赞、取消点赞 Type
 * @Author: gchong3068
 * @Date: 2025/6/2 20:16
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum LikeUnlikeCommentTypeEnum {

    LIKE(1),
    UNLIKE(0),
    ;

    private final Integer code;

    /**
     * 根据code获取枚举
     * @Auther: gchong3068
     * @Date: 2025/6/2 20:24
     * @param code
     * @return com.gchong3068.xiaohongshu.count.biz.enums.LikeUnlikeCommentTypeEnum
     **/
    public static LikeUnlikeCommentTypeEnum valueOf(Integer code) {
        for (LikeUnlikeCommentTypeEnum likeUnlikeCommentTypeEnum : LikeUnlikeCommentTypeEnum.values()){
            if (Objects.equals(likeUnlikeCommentTypeEnum.getCode(), code)){
                return likeUnlikeCommentTypeEnum;
            }
        }
        return null;
    }
}
