package com.back.wdam.module.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InitDto {
    private  String unitName;

    private String symbol;

    private String status;

    private String member;

    private String equipment;

    private String supply;

    private LocalDateTime createdAt;

    public InitDto(String unitName, String symbol, String status, String member, String equipment, String supply, LocalDateTime createdAt){
        this.unitName = unitName;
        this.symbol = symbol;
        this.status = status;
        this.member = member;
        this.equipment = equipment;
        this.supply = supply;
        this.createdAt = createdAt;
    }
}
