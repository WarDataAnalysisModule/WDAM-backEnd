package com.back.wdam.util.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //behavior 파일이 없을 경우
    BEHAVIOR_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "behaivor 파일을 업로드 해주세요.");



    private final HttpStatus status;
    private final String code;
    private final String message;
}