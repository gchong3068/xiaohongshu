package com.gchong3068.xiaohongshu.note.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 笔记可见性枚举
 * @Author: gchong3068
 * @Date: 2024/8/14 19:28
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum NoteVisibleEnum {
    PUBLIC(0), // 公开，所有人可见
    PRIVATE(1); // 仅自己可见

    private final Integer code;
}
