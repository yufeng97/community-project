package com.nowcoder.community.exception;

public class InvalidCredentialsException extends UserException{

    public InvalidCredentialsException() {
        super("user.credential.error", null);
    }
}
