package com.nowcoder.community.exception;

public class UserInvalidStateException extends UserException{

    public UserInvalidStateException() {
        super("user.not.activate", null);
    }
}
