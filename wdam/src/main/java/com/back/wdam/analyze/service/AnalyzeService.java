package com.back.wdam.analyze.service;

import com.back.wdam.analyze.dto.AnalyzeResultDto;
import com.back.wdam.entity.ResultLog;
import com.back.wdam.entity.Users;
import com.back.wdam.log.repository.LogRepository;
import com.back.wdam.user.repository.UserRepository;
import com.back.wdam.util.exception.CustomException;
import com.back.wdam.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class AnalyzeService {

    private final UserRepository userRepository;
    private final LogRepository logRepository;

    public Long saveNewAnalyzeResult(Long userIdx, String analysisFeature, String result, LocalDateTime logCreated) {

        Users users = getUserById(userIdx);
        ResultLog resultLog = logRepository.saveAndFlush(new ResultLog(users, analysisFeature, result, logCreated));
        return resultLog.getLogIdx();
    }

    public List<AnalyzeResultDto> getAnalyzeResults(Long userIdx, LocalDateTime logCreated) {

        List<ResultLog> resultLogs = logRepository.findByUserIdAndLogCreated(userIdx, logCreated);
        if(resultLogs.isEmpty())
            throw new CustomException(ErrorCode.RESULTLOG_NOT_FOUND);

        List<AnalyzeResultDto> analyzeResultDtos = new ArrayList<>();
        for(ResultLog resultLog : resultLogs) {
            analyzeResultDtos.add(new AnalyzeResultDto(resultLog));
        }
        return analyzeResultDtos;
    }

    private Users getUserById(Long userIdx) {

        Optional<Users> users = userRepository.findByUserIdx(userIdx);
        if(users.isEmpty())
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        return users.get();
    }
}
