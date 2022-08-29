package com.nowcoder.community.util;

public enum ResultCode {

    SUCCESS(200, "Success"),
    FAILED(500, "Failed"),
    VALIDATE_FAILED(404, "Not found"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    BAD_REQUEST(400, "Bad Request");
    private int code;
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

}
