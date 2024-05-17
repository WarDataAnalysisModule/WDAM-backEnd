package com.back.wdam.log.dto;

import com.back.wdam.entity.ResultLog;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LogResultDto {

    private Long logIdx;
    private String analysisFeature;
    private String result;
    private LocalDateTime logCreated;
    private LocalDateTime createdAt;

    public LogResultDto(ResultLog resultLog) {
        this.logIdx = resultLog.getLogIdx();
        this.analysisFeature = resultLog.getAnalysisFeature();
        this.result = resultLog.getResult();
        this.logCreated = resultLog.getLogCreated();
        this.createdAt = resultLog.getCreatedAt();
    }
}
