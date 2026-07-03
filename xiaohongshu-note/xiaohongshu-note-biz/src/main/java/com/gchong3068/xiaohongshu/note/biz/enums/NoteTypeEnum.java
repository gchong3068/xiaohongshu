package com.gchong3068.xiaohongshu.note.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 笔记类型枚举
 * @Author: gchong3068
 * @Date: 2024/8/14 19:28
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum NoteTypeEnum {

    IMAGE_TEXT(0, "图文"),
    VIDEO(1, "视频");

    private final Integer code;
    private final String description;

    /**
     * 判断类型是否有效
     * @Auther: gchong3068
     * @Date: 2024/8/14 19:32
     * @param code 类型码
     * @return boolean
     **/
    public static boolean isValid(Integer code){
        for (NoteTypeEnum noteTypeEnum : NoteTypeEnum.values()){
            if (Objects.equals(code,noteTypeEnum.getCode())){
                return true;
            }
        }
        return false;
    }

    /**
     * 根据类型获取对应字段
     * @Auther: gchong3068
     * @Date: 2024/8/14 19:34
 	 * @param code 类型码
 	 * @return com.gchong3068.xiaohongshu.note.biz.enums.NoteTypeEnum
     **/
    public static NoteTypeEnum valueOf(Integer code){
        for (NoteTypeEnum noteTypeEnum : NoteTypeEnum.values()){
            if (Objects.equals(code,noteTypeEnum.getCode())){
                return noteTypeEnum;
            }
        }
        return null;
    }

}
