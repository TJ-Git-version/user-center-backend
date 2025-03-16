package com.surfer.usercenter.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一返回结果
 *
 * @author Dev Surfer
 */
@Data
public class BaseResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 3916228848334830917L;

    private int code;

    private T data;

    private String message;

    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "ok", "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
