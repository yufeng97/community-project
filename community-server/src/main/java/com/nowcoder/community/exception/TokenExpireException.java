package com.nowcoder.community.exception;

public class TokenExpireException extends UnAuthorizationException {
    public TokenExpireException(String code, String message) {
        super(code, message);
    }

    public TokenExpireException() {
        this("token.expired.error", null);
    }
}
