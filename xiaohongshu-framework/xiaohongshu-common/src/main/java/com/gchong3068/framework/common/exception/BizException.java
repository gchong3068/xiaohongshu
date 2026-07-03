package com.gchong3068.framework.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author gc
 * @date 2024年05月04日 19:17
 */
@Getter
@Setter
public class BizException extends RuntimeException{

    private String errorCode;
    private String errorMessage;

    public BizException(BaseExceptionInterface baseExceptionInterface){
        this.errorCode = baseExceptionInterface.getErrorCode();
        this.errorMessage = baseExceptionInterface.getErrorMessage();
    }

}
