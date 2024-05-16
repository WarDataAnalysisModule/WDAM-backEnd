package com.back.wdam.entity;

import com.back.wdam.file.dto.UpperDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "upper_attributes")
public class UpperAttributes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upper_idx")
    private Long upperIdx;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "list_idx")
    private UnitList unitList;

    private Long simulationTime;

    private String unitName;

    private String uuid;

    private String subordinateID;

    private Integer forceIdentifier;

    private String damageState;

    private LocalDateTime createdAt;

    public UpperAttributes(UnitList newUnit, UpperDto upperDto) {
        this.unitList = newUnit;
        this.simulationTime = upperDto.getSimulationTime();
        this.unitName = upperDto.getUnitName();
        this.uuid = upperDto.getUuid();
        this.subordinateID = upperDto.getSubordinateID();
        this.forceIdentifier = upperDto.getForceIdentifier();
        this.damageState = upperDto.getDamageState();
        this.createdAt = upperDto.getCreatedAt();
    }

    public UpperAttributes() {

    }
}
