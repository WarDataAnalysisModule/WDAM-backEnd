package com.back.wdam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class ResultLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_idx")
    private Long logIdx;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private Users users;

    private String analysisFeature;

    @Column(name = "result", length = 10000)
    private String result;

    private LocalDateTime logCreated;   // 로그 생성일자
    private LocalDateTime createdAt;    // 시뮬레이션 일자

    public ResultLog() {}

    public ResultLog(Users users, String analysisFeature, String result, LocalDateTime logCreated) {
        this.users = users;
        this.analysisFeature = analysisFeature;
        this.result = result;
        this.logCreated = logCreated;
        this.createdAt = LocalDateTime.now();
    }
}
