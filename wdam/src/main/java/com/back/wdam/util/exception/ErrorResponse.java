package com.back.wdam.util.exception;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ErrorResponse<T> {

    private String code;
    private T data;

    @Builder
    public ErrorResponse(String code) {
        this.code = code;
        this.data = null;
    }
}