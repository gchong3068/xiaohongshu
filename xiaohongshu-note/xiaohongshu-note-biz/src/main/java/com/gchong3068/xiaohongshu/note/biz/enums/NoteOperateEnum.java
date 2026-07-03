package com.gchong3068.xiaohongshu.note.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 笔记操作枚举
 * @Author: gchong3068
 * @Date: 2024/11/2 17:26
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum NoteOperateEnum {
    PUBLISH(1),
    DELETE(0),
    ;

    private final Integer code;

    public static NoteOperateEnum valueOf(Integer code){
        for (NoteOperateEnum noteOperateEnum : NoteOperateEnum.values()){
            if (Objects.equals(code, noteOperateEnum.getCode())){
                return noteOperateEnum;
            }
        }
        return null;
    }
}
