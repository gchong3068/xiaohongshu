package com.gchong3068.xiaohongshu.search.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 笔记状态枚举
 * @Author: gchong3068
 * @Date: 2024/12/17 19:48
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum NoteStatusEnum {

    BE_EXAMINE(0), // 待审核
    NORMAL(1), // 正常展示
    DELETED(2), // 被删除
    DOWNED(3), // 被下架
    ;

    private final Integer code;
}
