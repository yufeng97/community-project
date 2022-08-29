package com.nowcoder.community.exception;

public class InvalidTokenException extends BadRequestException{

    public InvalidTokenException(String code, String defaultMessage) {
        super(code, defaultMessage);
    }

    public InvalidTokenException(String defaultMessage) {
        super(null, defaultMessage);
    }

    public InvalidTokenException() {
        super("token.invalid.error", null);
    }
}
