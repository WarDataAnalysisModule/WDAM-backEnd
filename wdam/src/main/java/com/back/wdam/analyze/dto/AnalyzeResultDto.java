package com.back.wdam.analyze.dto;

import com.back.wdam.entity.ResultLog;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AnalyzeResultDto {

    private Long logIdx;
    private String analysisFeature;
    private String result;
    private LocalDateTime logCreated;
    private LocalDateTime createdAt;

    public AnalyzeResultDto(ResultLog resultLog) {
        this.logIdx = resultLog.getLogIdx();
        this.analysisFeature = resultLog.getAnalysisFeature();
        this.result = resultLog.getResult();
        this.logCreated = resultLog.getLogCreated();
        this.createdAt = resultLog.getCreatedAt();
    }
}
