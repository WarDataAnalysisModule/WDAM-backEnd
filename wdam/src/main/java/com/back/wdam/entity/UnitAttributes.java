package com.back.wdam.entity;

import com.back.wdam.module.dto.UnitDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "UnitAttributes")
public class UnitAttributes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attributesIdx")
    private Long attributesIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unitId", referencedColumnName = "unitId") // unitId를 외래 키로 사용
    private UnitList unitList;

    private Long simulationTime;

    private String unitName;

    private String uuid;

    private Integer forceIdentifier;

    private String entitySymbol;

    private Double positionLat;

    private Double positionOn;

    private Double positionAlt;

    private Double orientation;

    private Double speed;

    private String damageState;

    private Integer power;

    private String powerDistribution;

    private String detectedEntityId;

    private String detectedEntityDistance;

    private String echelon;

    private String mos;

    private LocalDateTime createdAt;

    public UnitAttributes(UnitList newUnit, UnitDto unitDto) {
        this.unitList = newUnit;
        this.simulationTime = unitDto.getSimulationTime();
        this.unitName = unitDto.getUnitName();
        this.uuid = unitDto.getUuid();
        this.forceIdentifier = unitDto.getForceIdentifier();
        this.entitySymbol = unitDto.getEntitySymbol();
        this.positionLat = unitDto.getPositionLat();
        this.positionOn = unitDto.getPositionOn();
        this.positionAlt = unitDto.getPositionAlt();
        this.orientation = unitDto.getOrientation();
        this.speed = unitDto.getSpeed();
        this.damageState = unitDto.getDamageState();
        this.power = unitDto.getPower();
        this.powerDistribution = unitDto.getPowerDistribution();
        this.detectedEntityId = unitDto.getDetectedEntityId();
        this.detectedEntityDistance = unitDto.getDetectedEntityDistance();
        this.echelon = unitDto.getEchelon();
        this.mos = unitDto.getMos();
        this.createdAt = unitDto.getCreatedAt();
    }
}
