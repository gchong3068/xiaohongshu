package com.gchong3068.xiaohongshu.count.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 笔记收藏、取消收藏 Type
 * @Author: gchong3068
 * @Date: 2024/10/31 15:21
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum CollectUnCollectNoteTypeEnum {

    COLLECT(1),
    UN_COLLECT(0)
    ;

    private final Integer type;

    public static CollectUnCollectNoteTypeEnum valueOf(Integer type) {
        for (CollectUnCollectNoteTypeEnum item : CollectUnCollectNoteTypeEnum.values()){
            if (Objects.equals(item.type, type)){
                return item;
            }
        }
        return null;
    }
}
