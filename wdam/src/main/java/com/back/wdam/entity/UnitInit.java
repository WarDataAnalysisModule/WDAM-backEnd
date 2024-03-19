package com.back.wdam.entity;

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

    private Integer unitId;

    private  String unitName;

    private String symbol;

    private String status;

    private String member;

    private String equipment;

    private String supply;

    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
