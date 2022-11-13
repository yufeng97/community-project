package com.nowcoder.community.exception;

public class CommentNotExistException extends NotFoundException{
    public CommentNotExistException() {
        super("comment.deleted.error", null);
    }
}
