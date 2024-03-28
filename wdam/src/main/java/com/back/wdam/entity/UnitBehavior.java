package com.back.wdam.entity;

import com.back.wdam.module.dto.BehaviorDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unitId", referencedColumnName = "unitId") // unitId를 외래 키로 사용
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

