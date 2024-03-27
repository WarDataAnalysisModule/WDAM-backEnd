package com.back.wdam.entity;

import com.back.wdam.module.dto.InitDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unitId", referencedColumnName = "unitId") // unitId를 외래 키로 사용
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
}
