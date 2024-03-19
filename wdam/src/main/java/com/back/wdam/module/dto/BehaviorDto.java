package com.back.wdam.module.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BehaviorDto {
    private Integer simulationTime;
    private String behaviorName;
    private String status;

    public BehaviorDto(Integer simulationTime, String behaviorName, String status){
        this.simulationTime = simulationTime;
        this.behaviorName = behaviorName;
        this.status = status;
    }
}
