package com.surfer.usercenter.common;

/**
 * 封装统一返回结果调用
 *
 * @author Dev Surfer
 */
@SuppressWarnings("all")
public class ResultUtils {

    /**
     * 成功返回结果
     * @param data                  数据载体
     * @param <T>                   返回数据类型
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok", "");
    }

    /**
     * 失败返回结果
     * @param errorCode             错误码
     */
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败返回结果
     * @param errorCode             错误码
     */
    public static BaseResponse error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, message, description);
    }

    /**
     * 失败返回结果
     * @param errorCode             错误码
     */
    public static BaseResponse error(int errorCode, String message, String description) {
        return new BaseResponse<>(errorCode, null, message, description);
    }

}
