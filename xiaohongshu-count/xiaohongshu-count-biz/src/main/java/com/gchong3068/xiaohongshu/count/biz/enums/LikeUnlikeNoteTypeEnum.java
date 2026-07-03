package com.gchong3068.xiaohongshu.count.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 笔记点赞、取消点赞 Type
 * @Author: gchong3068
 * @Date: 2024/10/27 20:47
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum LikeUnlikeNoteTypeEnum {
    // 点赞
    LIKE(1),
    // 取消点赞
    UNLIKE(0),
    ;

    private final Integer code;

    /**
     * 根据code获取枚举
     * @Auther: gchong3068
     * @Date: 2025/5/30 11:05
 	 * @param code
 	 * @return com.gchong3068.xiaohongshu.count.biz.enums.LikeUnlikeNoteTypeEnum
     **/
    public static LikeUnlikeNoteTypeEnum valueOf(Integer code){
        for (LikeUnlikeNoteTypeEnum likeUnlikeNoteTypeEnum : LikeUnlikeNoteTypeEnum.values()){
            if (Objects.equals(code,likeUnlikeNoteTypeEnum.getCode())){
                return likeUnlikeNoteTypeEnum;
            }
        }
        return null;
    }
}
