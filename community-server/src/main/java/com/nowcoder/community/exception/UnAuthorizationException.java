package com.nowcoder.community.exception;

/**
 * 权限异常类
 *
 * @author ruoyi
 */
public class UnAuthorizationException extends BaseException {
    private static final long serialVersionUID = 1L;

    public UnAuthorizationException(String code, Object[] args, String message) {
        super("auth", code, args, message);
    }

    public UnAuthorizationException(String code, String message) {
        this(code, null, message);
    }

    public UnAuthorizationException(String message) {
        this(null, message);
    }

    public UnAuthorizationException() {
        this("请登录");
    }
}

