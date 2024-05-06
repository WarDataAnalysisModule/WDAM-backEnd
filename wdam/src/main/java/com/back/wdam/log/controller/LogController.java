package com.back.wdam.log.controller;

import com.back.wdam.log.dto.LogDto;
import com.back.wdam.log.service.LogService;
import com.back.wdam.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
public class LogController {

    private final LogService logService;

//    @GetMapping("/{logCreated}")
//    ResponseEntity<ApiResponse<List<LogDto>>> getLogs(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("logCreated")LocalDateTime logCreated) {
//        logCreatedString = logCreatedString.replaceAll("\\.\\d+$", "");
//        LocalDateTime logCreated = LocalDateTime.parse(logCreatedString);
//        return ResponseEntity.ok(ApiResponse.success((logService.getLogs(userDetails, logCreated))));
//    }
    @GetMapping("/{logCreated}")
    ResponseEntity<ApiResponse<List<LogDto>>> getLogs(@PathVariable("logCreated") String logCreatedString) {
        logCreatedString = logCreatedString.replaceAll("\\.\\d+$", "");
        LocalDateTime logCreated = LocalDateTime.parse(logCreatedString);
        return ResponseEntity.ok(ApiResponse.success((logService.getLogs(null, logCreated))));
    }
}
