package com.back.wdam.log.service;

import com.back.wdam.entity.ResultLog;
import com.back.wdam.entity.UnitList;
import com.back.wdam.entity.Users;
import com.back.wdam.file.repository.UnitListRepository;
import com.back.wdam.log.dto.LogDto;
import com.back.wdam.log.dto.LogResultDto;
import com.back.wdam.log.repository.LogRepository;
import com.back.wdam.user.repository.UserRepository;
import com.back.wdam.util.exception.CustomException;
import com.back.wdam.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class LogService {

    private final UserRepository userRepository;
    private final LogRepository logRepository;
    private final UnitListRepository unitListRepository;

    public LogDto getLogs(UserDetails userDetails, LocalDateTime logCreated) {

        Users user = getUserByName(userDetails);

        List<ResultLog> resultLogs = logRepository.findByUserIdAndLogCreated(user.getUserIdx(), logCreated);
        if(resultLogs.isEmpty())
            throw new CustomException(ErrorCode.RESULTLOG_NOT_FOUND);

        List<LogResultDto> logResultDtos = new ArrayList<>();
        for (ResultLog resultLog : resultLogs) {
            logResultDtos.add(new LogResultDto(resultLog));
        }

        Optional<List<UnitList>> unitList = unitListRepository.findAllByUserIdxAndLogCreated(user.getUserIdx(), logCreated);
        if(unitList.isEmpty() || unitList.get().isEmpty())
            throw new CustomException(ErrorCode.UNIT_LIST_NOT_FOUND);

        LogDto logDto = new LogDto(unitList.get(), logResultDtos);
        return logDto;
    }

    private Users getUserByName(UserDetails userDetails) {

        Optional<Users> users = userRepository.findByUserName(userDetails.getUsername());
        if(users.isEmpty())
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        return users.get();
    }
}
