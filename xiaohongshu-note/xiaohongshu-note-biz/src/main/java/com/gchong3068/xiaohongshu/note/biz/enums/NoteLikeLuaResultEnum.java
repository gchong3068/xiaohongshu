package com.gchong3068.xiaohongshu.note.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 点赞，Lua脚本返回
 * @Author: gchong3068
 * @Date: 2024/10/17 10:37
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum NoteLikeLuaResultEnum {

    NOTE_LIKE_SUCCESS(0L),

    NOT_EXIST(-1L),

    NOTE_LIKED(1L),
    ;

    private final Long code;

    /**
     * 根据code 获取对应枚举
     * @Auther: gchong3068
     * @Date: 2024/10/17 10:41
 	 * @param code code
 	 * @return com.gchong3068.xiaohongshu.note.biz.enums.NoteLikeLuaResultEnum
     **/
    public static NoteLikeLuaResultEnum valueOf(Long code) {
        for (NoteLikeLuaResultEnum noteLikeLuaResultEnum : NoteLikeLuaResultEnum.values()){
            if (Objects.equals(code,noteLikeLuaResultEnum.getCode())){
                return noteLikeLuaResultEnum;
            }
        }
        return null;
    }

}
