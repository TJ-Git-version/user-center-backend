package com.surfer.usercenter.exception;

import com.surfer.usercenter.common.BaseResponse;
import com.surfer.usercenter.common.ErrorCode;
import com.surfer.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局统一异常处理器
 *
 * @author Dev Surfer
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException ex) {
        log.error("businessException: " + ex.getMessage(), ex);
        return ResultUtils.error(ex.getCode(), ex.getMessage(), ex.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException ex) {
        log.error("runtimeException", ex);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, ex.getMessage(), "");
    }

}
