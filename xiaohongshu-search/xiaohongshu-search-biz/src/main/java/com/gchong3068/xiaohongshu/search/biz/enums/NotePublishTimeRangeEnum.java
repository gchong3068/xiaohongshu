package com.gchong3068.xiaohongshu.search.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 笔记发布时间范围枚举
 * @Author: gchong3068
 * @Date: 2024/12/7 22:25
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum NotePublishTimeRangeEnum {

    DAY(0), //一天

    WEEK(1), //一周

    HALF_YEAR(2),// 半年内

    ;

    private final Integer code;


    /**
     * 根据code 获取对应枚举
     * @Auther: gchong3068
     * @Date: 2024/12/7 22:26
     * @param code code
     * @return com.gchong3068.xiaohongshu.search.enums.NotePublishTimeRangeEnum
     **/
    public static NotePublishTimeRangeEnum valueOf(Integer code) {
        for (NotePublishTimeRangeEnum notePublishTimeRangeEnum : NotePublishTimeRangeEnum.values()) {
            if (Objects.equals(code, notePublishTimeRangeEnum.getCode())) {
                return notePublishTimeRangeEnum;
            }
        }
        return null;
    }




}
