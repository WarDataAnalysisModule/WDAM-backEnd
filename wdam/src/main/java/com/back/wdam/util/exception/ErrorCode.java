package com.back.wdam.util.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    CHARACTERISTIC_INVALID(HttpStatus.BAD_REQUEST, "300"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "400"),
    RESULTLOG_NOT_FOUND(HttpStatus.NOT_FOUND, "401"),
    UNIT_LIST_NOT_FOUND(HttpStatus.NOT_FOUND, "402"),
    BEHAVIOR_NOT_FOUND(HttpStatus.NOT_FOUND, "403"),
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "404"),
    UNIT_INIT_NOT_FOUND(HttpStatus.NOT_FOUND, "405"),
    UNIT_ATTRIBUTES_NOT_FOUND(HttpStatus.NOT_FOUND, "406"),
    UPPER_ATTRIBUTES_NOT_FOUND(HttpStatus.NOT_FOUND, "407"),

    PREPROCESSED_DATA_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "500"),
    RESULT_NOT_CREATED(HttpStatus.INTERNAL_SERVER_ERROR, "501"),
    DATA_SAVE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "502"),
    PROCESS_EXECUTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "503");

    private final HttpStatus status;
    private final String code;
}