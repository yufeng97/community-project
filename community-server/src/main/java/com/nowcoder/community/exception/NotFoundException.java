package com.nowcoder.community.exception;

public class NotFoundException extends BaseException{
    public NotFoundException(String code, String defaultMessage) {
        super(null, code, null, defaultMessage);
    }

    public NotFoundException(String defaultMessage) {
        this(null, defaultMessage);
    }
}
