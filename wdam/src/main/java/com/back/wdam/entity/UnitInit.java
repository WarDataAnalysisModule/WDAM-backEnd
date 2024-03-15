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
@Table(name = "UnitInit")

public class UnitInit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "initIdx")
    private Long initIdx;

    private Integer unitId;

    private  String unitName;

    private String symbol;

    private String status;

    private String member;

    private String equipment;

    private String supply;

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
