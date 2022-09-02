package com.nowcoder.community.exception;

public class PostNotExistException extends NotFoundException {
    public PostNotExistException(String defaultMessage) {
        super(defaultMessage);
    }

    public PostNotExistException() {
        super("post.deleted.error", null);
    }
}
