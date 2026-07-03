package com.gchong3068.xiaohongshu.note.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 笔记收藏：执行 Lua 脚本返回结果
 * @Author: gchong3068
 * @Date: 2024/10/27 21:35
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum NoteCollectLuaResultEnum {

    // 布隆过滤器或者 ZSet 不存在
    NOT_EXIST(-1L),
    // 笔记已收藏
    NOTE_COLLECTED(1L),
    // 笔记收藏成功
    NOTE_COLLECTED_SUCCESS(0L),
    ;

    private final Long code;

    public static NoteCollectLuaResultEnum valueOf(Long code){
        for (NoteCollectLuaResultEnum  noteCollectLuaResultEnum  : NoteCollectLuaResultEnum.values()){
            if (Objects.equals(code, noteCollectLuaResultEnum.getCode())){
                return noteCollectLuaResultEnum;
            }
        }
        return null;
    }
}
