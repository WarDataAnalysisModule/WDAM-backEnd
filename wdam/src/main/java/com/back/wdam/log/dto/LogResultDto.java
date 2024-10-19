package com.back.wdam.log.dto;

import com.back.wdam.entity.ResultLog;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LogResultDto {

    private Long logIdx;
    private String unitName;
    private String analysisFeature;
    private String result;
    private String imgUrl;
    private LocalDateTime logCreated;
    private LocalDateTime createdAt;

    public LogResultDto(ResultLog resultLog) {
        this.logIdx = resultLog.getLogIdx();
        this.unitName = resultLog.getUnitName();
        this.analysisFeature = resultLog.getAnalysisFeature();
        this.result = resultLog.getResult();
        this.imgUrl = resultLog.getImgUrl();
        this.logCreated = resultLog.getLogCreated();
        this.createdAt = resultLog.getCreatedAt();
    }
}
