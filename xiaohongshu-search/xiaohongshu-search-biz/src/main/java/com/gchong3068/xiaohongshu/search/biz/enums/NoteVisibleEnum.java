package com.gchong3068.xiaohongshu.search.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 笔记可见性
 * @Author: gchong3068
 * @Date: 2024/12/17 19:49
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum NoteVisibleEnum {

    PUBLIC(0),
    PRIVATE(1);


    private final Integer code;


}
