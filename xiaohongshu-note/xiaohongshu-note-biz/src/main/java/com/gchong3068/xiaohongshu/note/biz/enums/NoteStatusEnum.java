package com.gchong3068.xiaohongshu.note.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 笔记状态枚举
 * @Author: gchong3068
 * @Date: 2024/8/14 19:27
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum NoteStatusEnum {

    BE_EXAMINE(0), // 待审核
    NORMAL(1), // 正常展示
    DELETED(2), // 被删除
    DOWNED(2), // 被下架
    ;

    private final Integer code;
}
