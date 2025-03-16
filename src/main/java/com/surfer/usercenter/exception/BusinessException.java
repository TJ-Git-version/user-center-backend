package com.surfer.usercenter.exception;

import com.surfer.usercenter.common.ErrorCode;
import lombok.Getter;

import java.io.Serial;

/**
 * 自定义异常封装
 *
 * @author Dev Surfer
 */
@Getter
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7544321243890628899L;

    private final int code;

    private final String description;

    public BusinessException(int code, String message, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

}
