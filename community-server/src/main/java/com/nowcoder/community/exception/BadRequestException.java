package com.nowcoder.community.exception;

public class BadRequestException extends BaseException{
    public BadRequestException(String code, String defaultMessage) {
        super(null, code, null, defaultMessage);
    }

    public BadRequestException(String defaultMessage) {
        this(null, defaultMessage);
    }
}
