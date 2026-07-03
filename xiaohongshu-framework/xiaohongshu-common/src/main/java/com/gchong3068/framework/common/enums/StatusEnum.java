package com.gchong3068.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author gc
 * @date 2024年05月31日 20:22
 */

@Getter
@AllArgsConstructor
public enum StatusEnum {

    // 启用
    ENABLE(0),
    // 禁用
    DISABLED(1);

    private final Integer value;
}
