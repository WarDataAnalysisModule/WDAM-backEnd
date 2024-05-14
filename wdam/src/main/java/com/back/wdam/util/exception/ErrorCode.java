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
    RESULTLOG_NOT_FOUND(HttpStatus.NOT_FOUND, "404"),

    // preprocessedData가 null일 경우
    PREPROCESSED_DATA_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "500"),
    RESULT_NOT_CREATED(HttpStatus.INTERNAL_SERVER_ERROR, "500"),
    DATA_SAVE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "500"),
    PROCESS_EXECUTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500");

    private final HttpStatus status;
    private final String code;
}