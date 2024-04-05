package com.back.wdam.entity;

import com.back.wdam.file.dto.BehaviorDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "UnitBehavior")

public class UnitBehavior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "behaviorIdx")
    private Long behaviorIdx;

    @ManyToOne
    @JoinColumn(name = "listIdx")
    private UnitList unitList;

    private Long simulationTime;

    private String behaviorName;

    private String status;

    private LocalDateTime createdAt;

    public UnitBehavior(UnitList unitList, BehaviorDto behaviorDto){
        this.unitList = unitList;
        this.simulationTime = behaviorDto.getSimulationTime();
        this.behaviorName = behaviorDto.getBehaviorName();
        this.status = behaviorDto.getStatus();
        this.createdAt = behaviorDto.getCreatedAt();
    }

    public UnitBehavior() {

    }
}

