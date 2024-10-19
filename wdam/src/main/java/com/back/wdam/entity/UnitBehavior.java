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
@Table(name = "unit_behavior")

public class UnitBehavior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "behavior_idx")
    private Long behaviorIdx;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "list_idx")
    private UnitList unitList;

    private Long simulationTime;

    private String behaviorName;

    private String status;

    private LocalDateTime createdAt;

    public UnitBehavior(UnitList unitList, BehaviorDto behaviorDto){
        this.users = unitList.getUsers();
        this.unitList = unitList;
        this.simulationTime = behaviorDto.getSimulationTime();
        this.behaviorName = behaviorDto.getBehaviorName();
        this.status = behaviorDto.getStatus();
        this.createdAt = behaviorDto.getCreatedAt();
    }

    public UnitBehavior() {

    }
}

