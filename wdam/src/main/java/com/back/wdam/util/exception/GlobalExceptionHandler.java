package com.back.wdam.util.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = CustomException.class)

    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getCode().getCode())
                .build();
        return ResponseEntity.status(e.getCode().getStatus())
                .body(errorResponse);
    }
}