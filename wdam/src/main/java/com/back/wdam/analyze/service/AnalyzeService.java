package com.back.wdam.analyze.service;

import com.back.wdam.analyze.dto.AnalyzeResultDto;
import com.back.wdam.entity.ResultLog;
import com.back.wdam.entity.Users;
import com.back.wdam.log.repository.LogRepository;
import com.back.wdam.user.repository.UserRepository;
import com.back.wdam.util.exception.CustomException;
import com.back.wdam.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public Long saveNewAnalyzeResult(UserDetails userDetails, String analysisFeature, String result, LocalDateTime logCreated) {

        try {
            Users user = getUserByName(userDetails);

            ResultLog resultLog = logRepository.saveAndFlush(new ResultLog(user, analysisFeature, result, logCreated));
            return resultLog.getLogIdx();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.DATA_SAVE_FAILURE);
        }
    }

    public List<AnalyzeResultDto> getAnalyzeResults(UserDetails userDetails, LocalDateTime logCreated) {

        Users user = getUserByName(userDetails);
        List<ResultLog> resultLogs = logRepository.findByUserIdAndLogCreated(user.getUserIdx(), logCreated);
        if(resultLogs.isEmpty())
            throw new CustomException(ErrorCode.RESULTLOG_NOT_FOUND);

        List<AnalyzeResultDto> analyzeResultDtos = new ArrayList<>();
        for(ResultLog resultLog : resultLogs) {
            analyzeResultDtos.add(new AnalyzeResultDto(resultLog));
        }
        return analyzeResultDtos;
    }

    public boolean sendAnalyzeDataToModule(UserDetails userDetails, String characteristics, String unit, LocalDateTime logCreated) {

        String line, preprocessedData = "", result = "";
        ProcessBuilder processBuilder;
        Process process;
        int exitCode;
        BufferedReader stdoutReader, stderrReader;

        try {
            // module1 실행
            processBuilder = new ProcessBuilder("python"
                    , "E:\\STUDY\\WDAM\\WDAM-backEnd\\wdam\\src\\main\\java\\com\\back\\wdam\\pythonModule\\module1.py"
                    ,  "\"" + characteristics + "\""
                    , unit);
            process = processBuilder.start();

            stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = stdoutReader.readLine()) != null) {
                if (line.startsWith("preprocessed_data:")) {
                    preprocessedData = stdoutReader.readLine().trim();
                }
            }
            if(preprocessedData.equals("")) {
                throw new CustomException(ErrorCode.PREPROCESSED_DATA_NULL);
            }

            stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = stderrReader.readLine()) != null) {
                System.out.println("Error: " + line);
            }

            exitCode = process.waitFor();
            System.out.println("Exited with error code " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        try {
            // module2 실행
            processBuilder = new ProcessBuilder("python"
                    , "E:\\STUDY\\WDAM\\WDAM-backEnd\\wdam\\src\\main\\java\\com\\back\\wdam\\pythonModule\\module2.py"
                    ,  "\"" + characteristics + "\""
                    , unit
                    , "\"" + preprocessedData + "\"");
            process = processBuilder.start();

            stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = stdoutReader.readLine()) != null) {
                if (line.startsWith("result: ")) {
                    result = stdoutReader.readLine().trim();
                }
            }
            if(result.equals("")) {
                throw new CustomException(ErrorCode.RESULT_NOT_CREATED);
            }

            stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = stderrReader.readLine()) != null) {
                System.out.println("Error: " + line);
            }

            exitCode = process.waitFor();
            System.out.println("Exited with error code " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        saveNewAnalyzeResult(userDetails, characteristics, result, logCreated);
        return true;
    }

    private Users getUserByName(UserDetails userDetails) {

        Optional<Users> users = userRepository.findByUserName(userDetails.getUsername());
        if(users.isEmpty())
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        return users.get();
    }
}
