package com.back.wdam.util.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //behavior 파일이 없을 경우
    BEHAVIOR_NOT_FOUND(HttpStatus.NOT_FOUND, "404"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404"),
    RESULTLOG_NOT_FOUND(HttpStatus.NOT_FOUND, "404");

    private final HttpStatus status;
    private final String code;
}