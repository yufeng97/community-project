package com.nowcoder.community.exception;

public class IllegalArgumentException extends BadRequestException {
    public IllegalArgumentException() {
        super("argument.error", null);
    }

    public IllegalArgumentException(String message) {
        super(message);
    }
}
