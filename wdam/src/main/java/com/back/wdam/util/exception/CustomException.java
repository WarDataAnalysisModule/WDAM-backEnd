package com.back.wdam.util.exception;

public class CustomException extends RuntimeException{

    private final ErrorCode code;

    public CustomException(ErrorCode code, String message){
        this.code = code ;
    }
    public CustomException(ErrorCode code) {
        this.code = code;
    }
    public ErrorCode getCode(){
        return this.code;
    }
}