package com.back.wdam.file.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventDto {
    private Long simulationTime;

    private String behaviorName;

    private String sourceMember;

    private String targetMember;

    private String sourceEquipment;

    private String targetEquipment;

    private String sourceSupply;

    private String targetSupply;

    private Integer distance;

    private String shellType;

    private String numShellUsed;

    private LocalDateTime createdAt;

    public EventDto(Long simulationTime, String behaviorName, String sourceMember, String targetMember,
                    String sourceEquipment, String targetEquipment, String sourceSupply, String targetSupply,
                    Integer distance, String shellType, String numShellUsed, LocalDateTime createdAt){
        this.simulationTime = simulationTime;
        this.behaviorName = behaviorName;
        this.sourceMember = sourceMember;
        this.targetMember = targetMember;
        this.sourceEquipment = sourceEquipment;
        this.targetEquipment = targetEquipment;
        this.sourceSupply = sourceSupply;
        this.targetSupply = targetSupply;
        this.distance = distance;
        this.shellType = shellType;
        this.numShellUsed = numShellUsed;
        this.createdAt = createdAt;
    }
}
