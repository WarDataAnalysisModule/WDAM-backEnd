package com.back.wdam.util.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    CHARACTERISTIC_INVALID(HttpStatus.BAD_REQUEST, "400"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404"),
    RESULTLOG_NOT_FOUND(HttpStatus.NOT_FOUND, "404"),
    UNIT_LIST_NOT_FOUND(HttpStatus.NOT_FOUND, "404"),
    BEHAVIOR_NOT_FOUND(HttpStatus.NOT_FOUND, "404"),
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "404"),
    UNIT_INIT_NOT_FOUND(HttpStatus.NOT_FOUND, "404"),
    UNIT_ATTRIBUTES_NOT_FOUND(HttpStatus.NOT_FOUND, "404"),
    UPPER_ATTRIBUTES_NOT_FOUND(HttpStatus.NOT_FOUND, "404"),

    // preprocessedData가 null일 경우
    PREPROCESSED_DATA_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "500"),
    RESULT_NOT_CREATED(HttpStatus.INTERNAL_SERVER_ERROR, "500"),
    DATA_SAVE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "500"),
    PROCESS_EXECUTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500");

    private final HttpStatus status;
    private final String code;
}