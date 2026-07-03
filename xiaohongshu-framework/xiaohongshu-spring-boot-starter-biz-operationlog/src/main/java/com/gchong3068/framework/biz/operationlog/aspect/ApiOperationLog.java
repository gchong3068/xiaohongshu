package com.gchong3068.framework.biz.operationlog.aspect;

import java.lang.annotation.*;

/**
 * @author gc
 * @date 2024年05月05日 20:30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ApiOperationLog {
    /**
     * API 功能描述
     * @author gc
     * @date 2024/5/5 20:51
     * @return java.lang.String
     */
    String description() default "";
}
