package com.back.wdam.file.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ListDto {

    private LocalDateTime simulationTime;
    private List<UnitListDto> unitList;

}