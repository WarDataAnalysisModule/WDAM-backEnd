package com.back.wdam.analyze.controller;

import com.back.wdam.analyze.dto.AnalyzeResultDto;
import com.back.wdam.analyze.dto.SendInfoDto;
import com.back.wdam.analyze.service.AnalyzeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.back.wdam.util.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analyze")
public class AnalyzeController {

    private final AnalyzeService analyzeService;

    @PostMapping("")
    ResponseEntity<ApiResponse<Boolean>> sendAnalyzeDataToModule(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody SendInfoDto sendInfoDto) {
        analyzeService.checkDataForAnalysis(userDetails, sendInfoDto.getCharacteristics(), sendInfoDto.getUnit(), sendInfoDto.getLogCreated());
        return ResponseEntity.ok(ApiResponse.success(analyzeService.sendAnalyzeDataToModule(userDetails, sendInfoDto.getCharacteristics(), sendInfoDto.getUnit(), sendInfoDto.getLogCreated())));
    }

    @PostMapping("/result")
    ResponseEntity<ApiResponse<List<AnalyzeResultDto>>> getAnalyzeResult(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("logCreated") LocalDateTime logCreated) {
        return ResponseEntity.ok(ApiResponse.success(analyzeService.getAnalyzeResults(userDetails, logCreated)));
    }
}
