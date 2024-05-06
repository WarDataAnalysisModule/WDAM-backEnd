package com.back.wdam.analyze.controller;

import com.back.wdam.analyze.dto.AnalyzeResultDto;
import com.back.wdam.analyze.service.AnalyzeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.back.wdam.util.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analyze")
public class AnalyzeController {

    private final AnalyzeService analyzeService;

    @PostMapping("")
    ResponseEntity<ApiResponse<Boolean>> sendAnalyzeDataToModule(@AuthenticationPrincipal UserDetails userDetails, String characteristics, String unit, LocalDateTime logCreated) {
        return ResponseEntity.ok(ApiResponse.success(analyzeService.sendAnalyzeDataToModule(userDetails, characteristics, unit, logCreated)));
    }

    @GetMapping("/result")
    ResponseEntity<ApiResponse<List<AnalyzeResultDto>>> getAnalyzeResult(@AuthenticationPrincipal UserDetails userDetails, String analysisFeature, String result, LocalDateTime logCreated) {
        return ResponseEntity.ok(ApiResponse.success(analyzeService.getAnalyzeResults(userDetails, logCreated)));
    }
}
