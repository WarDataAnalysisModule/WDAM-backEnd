package com.back.wdam.log.dto;

import com.back.wdam.entity.ResultLog;
import com.back.wdam.entity.UnitList;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class LogDto {

    private List<String> unitList;
    private List<LogResultDto> logResults;

    public LogDto(List<UnitList> unitLists, List<LogResultDto> logResults) {

        this.logResults = logResults;
        this.unitList = new ArrayList<>();
        for(UnitList unitName : unitLists) {
            this.unitList.add(unitName.getUnitName());
        }
    }
}
