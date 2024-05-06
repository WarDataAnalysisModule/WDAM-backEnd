package com.back.wdam.log.dto;

import com.back.wdam.entity.ResultLog;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LogDto {

    private Long logIdx;
    private String analysisFeature;
    private String result;
    private LocalDateTime logCreated;
    private LocalDateTime createdAt;

    public LogDto(ResultLog resultLog) {
        this.logIdx = resultLog.getLogIdx();
        this.analysisFeature = resultLog.getAnalysisFeature();
        this.result = resultLog.getResult();
        this.logCreated = resultLog.getLogCreated();
        this.createdAt = resultLog.getCreatedAt();
    }
}
