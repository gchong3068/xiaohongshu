package com.gchong3068.xiaohongshu.note.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 笔记收藏、取消收藏 Type
 * @Author: gchong3068
 * @Date: 2024/10/27 23:09
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum CollectUnCollectNoteTypeEnum {
    // 收藏
    COLLECT(1),
    // 取消收藏
    UN_COLLECT(0),
    ;

    private final Integer code;

    public static CollectUnCollectNoteTypeEnum valueOf(Integer code) {
        for (CollectUnCollectNoteTypeEnum item : CollectUnCollectNoteTypeEnum.values()){
            if (Objects.equals(code,item.getCode())){
                return item;
            }
        }
        return null;
    }
}
