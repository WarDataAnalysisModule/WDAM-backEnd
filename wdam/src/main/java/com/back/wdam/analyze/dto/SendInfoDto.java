package com.back.wdam.analyze.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SendInfoDto {

    private String characteristics;
    private String unit;
    private LocalDateTime logCreated;
}
