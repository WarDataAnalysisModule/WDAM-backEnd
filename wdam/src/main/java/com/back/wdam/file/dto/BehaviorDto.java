package com.back.wdam.file.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BehaviorDto {
    private Long simulationTime;
    private String behaviorName;
    private String status;
    private LocalDateTime createdAt;

    public BehaviorDto(Long simulationTime, String behaviorName, String status, LocalDateTime createdAt){
        this.simulationTime = simulationTime;
        this.behaviorName = behaviorName;
        this.status = status;
        this.createdAt = createdAt;
    }
}
