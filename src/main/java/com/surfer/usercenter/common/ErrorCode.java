package com.surfer.usercenter.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码
 *
 * @author Dev Surfer
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),
    NOT_LOGIN_ERROR(40100, "未登录", ""),
    NO_AUTH_ERROR(40101, "无权限", ""),
    SYSTEM_ERROR(50000, "系统内部异常", ""),
    ;

    /**
     * 状态码
     */
    private final int code;

    /**
     * 状态信息
     */
    private final String message;

    /**
     * 状态描述
     */
    private final String description;

}
