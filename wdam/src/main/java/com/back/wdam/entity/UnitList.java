package com.back.wdam.entity;

import com.back.wdam.enums.UpperUnit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "UnitList")
public class UnitList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "listIdx")
    private Long listIdx;

    private Long unitId;

    private String unitName;

    @Enumerated(EnumType.STRING)
    private UpperUnit status;
}
