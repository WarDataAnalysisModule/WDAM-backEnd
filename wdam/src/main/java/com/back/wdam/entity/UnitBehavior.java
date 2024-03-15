package com.back.wdam.entity;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "behaviorIdx")
    private Long behaviorIdx;

    @ManyToOne
    @JoinColumn(name = "unitId")
    private UnitInit unitInit;

    private Integer simulationTime;

    private String behaviorName;

    private String status;

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
