package com.gchong3068.xiaohongshu.note.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 取消收藏笔记 Lua 脚本返回值枚举
 * @Author: gchong3068
 * @Date: 2024/10/31 14:30
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum NoteUnCollectLuaResultEnum {

    NOT_EXIST(-1L),
    NOTE_COLLECTED(1L),
    // 笔记未收藏
    NOTE_NOT_COLLECTED(0L),
    ;

    private final Long code;

    public static NoteUnCollectLuaResultEnum valueOf(Long code){
        for (NoteUnCollectLuaResultEnum item : NoteUnCollectLuaResultEnum.values()){
            if (Objects.equals(item.getCode(),code)){
                return item;
            }
        }
        return null;
    }

}
