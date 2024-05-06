package com.back.wdam.log.service;

import com.back.wdam.entity.ResultLog;
import com.back.wdam.entity.Users;
import com.back.wdam.log.dto.LogDto;
import com.back.wdam.log.repository.LogRepository;
import com.back.wdam.user.repository.UserRepository;
import com.back.wdam.util.exception.CustomException;
import com.back.wdam.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
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

    public List<LogDto> getLogs(UserDetails userDetails, LocalDateTime logCreated) {

        //Users user = getUserByName(userDetails);

        //List<ResultLog> resultLogs = logRepository.findByUserIdAndLogCreated(user.getUserIdx(), logCreated);
        List<ResultLog> resultLogs = logRepository.findByUserIdAndLogCreated(3L, logCreated);
        if(resultLogs.isEmpty())
            throw new CustomException(ErrorCode.RESULTLOG_NOT_FOUND);

        List<LogDto> logDtos = new ArrayList<>();
        for (ResultLog resultLog : resultLogs) {
            logDtos.add(new LogDto(resultLog));
        }
        return logDtos;
    }

    private Users getUserByName(UserDetails userDetails) {

        Optional<Users> users = userRepository.findByUserName(userDetails.getUsername());
        if(users.isEmpty())
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        return users.get();
    }
}
