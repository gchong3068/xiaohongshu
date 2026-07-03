package com.gchong3068.xiaohongshu.user.relation.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: Lua 返回结果枚举
 * @Author: gchong3068
 * @Date: 2024/9/13 9:11
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum LuaResultEnum {

    // ZSET 不存在
    ZSET_NOT_EXIST(-1L),
    // 关注已达到上限
    FOLLOW_LIMIT(-2L),
    // 已经关注了该用户
    ALREADY_FOLLOWED(-3L),
    // 关注成功
    FOLLOW_SUCCESS(0L),
    // 未关注该用户
    NOT_FOLLOWED(-4L),
    ;

    private final Long code;

    /**
     * 根据code 获取对应枚举
     * @Auther: gchong3068
     * @Date: 2024/9/13 9:13
 	 * @param code Long
 	 * @return com.gchong3068.xiaohongshu.user.relation.biz.enums.LuaResultEnum
     **/
    public static LuaResultEnum valueOf(Long code){
        for (LuaResultEnum luaResultEnum : LuaResultEnum.values()){
            if (Objects.equals(code,luaResultEnum.getCode())){
                return luaResultEnum;
            }
        }
        return null;
    }

}
