package com.back.wdam.entity;

import com.back.wdam.file.dto.EventDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventIdx;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "source_list_idx")
    private UnitList sourceUnit;

    @ManyToOne
    @JoinColumn(name = "target_list_idx")
    private UnitList targetUnit;

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

    public Event(UnitList sourceUnit, UnitList targetUnit, EventDto eventDto) {
        this.users = sourceUnit.getUsers();
        this.sourceUnit = sourceUnit;
        this.targetUnit = targetUnit;
        this.simulationTime = eventDto.getSimulationTime();
        this.behaviorName = eventDto.getBehaviorName();
        this.sourceMember = eventDto.getSourceMember();
        this.targetMember = eventDto.getTargetMember();
        this.sourceEquipment = eventDto.getSourceEquipment();
        this.targetEquipment = eventDto.getTargetEquipment();
        this.sourceSupply = eventDto.getSourceSupply();
        this.targetSupply = eventDto.getTargetSupply();
        this.distance = eventDto.getDistance();
        this.shellType = eventDto.getShellType();
        this.numShellUsed = eventDto.getNumShellUsed();
        this.createdAt = eventDto.getCreatedAt();
    }

    public Event() {

    }
}
