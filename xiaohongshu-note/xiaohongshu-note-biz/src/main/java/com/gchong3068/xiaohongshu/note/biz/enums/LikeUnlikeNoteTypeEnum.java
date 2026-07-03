package com.gchong3068.xiaohongshu.note.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 笔记点赞/取消点赞Type
 * @Author: gchong3068
 * @Date: 2024/10/20 12:00
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum LikeUnlikeNoteTypeEnum {

    LIKE(1),

    UNLIKE(0),
    ;

    private final Integer code;

}
