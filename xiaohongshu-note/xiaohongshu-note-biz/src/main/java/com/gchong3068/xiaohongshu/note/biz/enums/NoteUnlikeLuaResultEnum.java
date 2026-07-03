package com.gchong3068.xiaohongshu.note.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 取消点赞笔记 Lua 脚本返回值枚举
 * @Author: gchong3068
 * @Date: 2024/10/22 20:48
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum NoteUnlikeLuaResultEnum {

    // 布隆过滤器不存在
    NOT_EXIST(-1L),
    // 笔记已点赞
    NOTE_LIKED(1L),
    // 笔记未点赞
    NOTE_NOT_LIKED(0L),
    ;

    private final Long code;

    public static NoteUnlikeLuaResultEnum valueOf(Long code){
        for (NoteUnlikeLuaResultEnum noteUnlikeLuaResultEnum : NoteUnlikeLuaResultEnum.values()){
            if (Objects.equals(code,noteUnlikeLuaResultEnum.getCode())){
                return noteUnlikeLuaResultEnum;
            }
        }
        return null;
    }
}
