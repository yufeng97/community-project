package com.nowcoder.community.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult<T> {

    private int code;
    private String message;
    private T data;

    protected CommonResult() {
    }

    protected CommonResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> CommonResult<T> success() {
        return success(null);
    }

    public static <T> CommonResult<T> fail(int code, String message) {
        return new CommonResult<>(code, message, null);
    }

    public static <T> CommonResult<T> fail(String message) {
        return new CommonResult<>(ResultCode.FAILED.getCode(), message, null);
    }

    public static <T> CommonResult<T> fail() {
        return fail(ResultCode.FAILED);
    }

    public static <T> CommonResult<T> fail(ResultCode code) {
        return new CommonResult<>(code.getCode(), code.getMessage(), null);
    }
}
