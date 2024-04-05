package com.back.wdam.file.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpperDto {
    private Long simulationTime;
    private String unitName;
    private String uuid;
    private String subordinateID;
    private Integer forceIdentifier;
    private String damageState;
    private LocalDateTime createdAt;

    public UpperDto(Long simulationTime, String unitName, String uuid, String subordinateID, Integer forceIdentifier,
                    String damageState, LocalDateTime createdAt){

        this.simulationTime = simulationTime;
        this.unitName = unitName;
        this.uuid = uuid;
        this.subordinateID = subordinateID;
        this.forceIdentifier = forceIdentifier;
        this.damageState = damageState;
        this.createdAt = createdAt;

    }
}
