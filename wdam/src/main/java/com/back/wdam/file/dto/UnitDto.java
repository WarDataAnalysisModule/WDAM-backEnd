package com.back.wdam.file.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UnitDto {
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
    private Double power;
    private String powerDistribution;
    private String detectedEntityId;
    private String detectedEntityDistance;
    private String echelon;
    private String mos;
    private LocalDateTime createdAt;

    public UnitDto(long simulationTime, String unitName, String uuid, Integer forceIdentifier,
                   String entitySymbol, Double positionLat, Double positionOn, Double positionAlt, Double orientation,
                   Double speed, String damageState, Double power, String powerDistribution, String detectedEntityId,
                   String detectedEntityDistance,
                   String echelon, String mos, LocalDateTime createdAt) {
        this.simulationTime = simulationTime;
        this.unitName = unitName;
        this.uuid = uuid;
        this.forceIdentifier = forceIdentifier;
        this.entitySymbol = entitySymbol;
        this.positionLat = positionLat;
        this.positionOn = positionOn;
        this.positionAlt = positionAlt;
        this.orientation = orientation;
        this.speed = speed;
        this.damageState = damageState;
        this.power = power;
        this.powerDistribution = powerDistribution;
        this.detectedEntityId = detectedEntityId;
        this.detectedEntityDistance = detectedEntityDistance;
        this.echelon = echelon;
        this.mos = mos;
        this.createdAt = createdAt;

    }
}
