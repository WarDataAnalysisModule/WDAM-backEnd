package com.back.wdam.entity;

import com.back.wdam.file.dto.InitDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString

public class UnitInit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "initIdx")
    private Long initIdx;

    @ManyToOne
    @JoinColumn(name = "listIdx")
    private UnitList unitList;

    private  String unitName;

    private String symbol;

    private String status;

    private String member;

    private String equipment;

    private String supply;

    private LocalDateTime createdAt;

    public UnitInit(UnitList newUnit, InitDto initDto) {
        this.unitList = newUnit;
        this.unitName = initDto.getUnitName();
        this.status = initDto.getStatus();
        this.member = initDto.getMember();
        this.equipment = initDto.getEquipment();
        this.supply = initDto.getSupply();
        this.createdAt = getCreatedAt();
    }

    public UnitInit() {

    }
}
