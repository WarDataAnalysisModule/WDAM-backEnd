package com.back.wdam.file.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitListDto {
    private Long unitListIdx;
    private String unitName;

    public UnitListDto(Long listIdx, String unitName) {
        this.unitListIdx = listIdx;
        this.unitName = unitName;
    }
}
