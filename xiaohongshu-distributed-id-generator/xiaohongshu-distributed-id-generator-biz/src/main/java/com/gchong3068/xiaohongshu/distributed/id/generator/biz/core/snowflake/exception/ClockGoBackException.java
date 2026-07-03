package com.gchong3068.xiaohongshu.distributed.id.generator.biz.core.snowflake.exception;

public class ClockGoBackException extends RuntimeException {
    public ClockGoBackException(String message) {
        super(message);
    }
}
