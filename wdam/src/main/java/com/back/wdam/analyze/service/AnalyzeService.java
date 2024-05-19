package com.back.wdam.analyze.service;

import com.back.wdam.analyze.dto.AnalyzeResultDto;
import com.back.wdam.entity.*;
import com.back.wdam.file.repository.*;
import com.back.wdam.log.repository.LogRepository;
import com.back.wdam.user.repository.UserRepository;
import com.back.wdam.util.exception.CustomException;
import com.back.wdam.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
    private final UnitListRepository unitListRepository;
    private final BehaviorRepository behaviorRepository;
    private final EventRepository eventRepository;
    private final InitRepository initRepository;
    private final UnitRepository unitRepository;
    private final UpperRepository upperRepository;

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

        String line = "", preprocessedData = "", result = "";
        ProcessBuilder processBuilder;
        Process process;
        int exitCode;

        try {
            processBuilder = new ProcessBuilder("python"
                    , "src\\main\\java\\com\\back\\wdam\\pythonModule\\module1.py"
                    , characteristics
                    , unit);
            process = processBuilder.start();

            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
            while ((line = stderrReader.readLine()) != null) {
                System.out.println("Error: " + line);
            }

            exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Process exited with error code " + exitCode);
            }

            StringBuilder outputBuilder = new StringBuilder();
            try (BufferedReader fileReader = new BufferedReader(new FileReader("src\\main\\java\\com\\back\\wdam\\analyze\\resources\\preprocessedData.txt"))) {
                while ((line = fileReader.readLine()) != null) {
                    outputBuilder.append(line).append("\n");
                }
                preprocessedData = outputBuilder.toString().trim();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.PROCESS_EXECUTION_ERROR);
        }

        if (preprocessedData.equals("")) {
            throw new CustomException(ErrorCode.PREPROCESSED_DATA_NULL);
        }

        try {
            // module2 실행
            processBuilder = new ProcessBuilder("python"
                    , "src\\main\\java\\com\\back\\wdam\\pythonModule\\module2.py"
                    , characteristics
                    , unit
                    , preprocessedData);
            process = processBuilder.start();

            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
            while ((line = stderrReader.readLine()) != null) {
                System.out.println("Error: " + line);
            }

            exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Process exited with error code " + exitCode);
            }

            StringBuilder outputBuilder = new StringBuilder();
            try (BufferedReader fileReader = new BufferedReader(new FileReader("src\\main\\java\\com\\back\\wdam\\analyze\\resources\\result.txt"))) {
                while ((line = fileReader.readLine()) != null) {
                    outputBuilder.append(line).append("\n");
                }
                result = outputBuilder.toString().trim();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.PROCESS_EXECUTION_ERROR);
        }

        if (result.equals("")) {
            throw new CustomException(ErrorCode.RESULT_NOT_CREATED);
        }

        saveNewAnalyzeResult(userDetails, characteristics, result, logCreated);
        return true;
    }

    public void checkDataForAnalysis(UserDetails userDetails, String characteristics, String unit, LocalDateTime logCreated) {

        Users user = getUserByName(userDetails);
        Optional<UnitList> unitList = unitListRepository.findByUserIdxAndUnitNameAndLogCreated(user.getUserIdx(), unit, logCreated);
        if(unitList.isEmpty()) {
            throw new CustomException(ErrorCode.UNIT_LIST_NOT_FOUND);
        }
        //final int UNIT_LIST_INDEX = 0;

        if(characteristics.equals("부대 행동")) {
            checkUnitBehavior(user.getUserIdx(), unitList.get().getListIdx(), logCreated);
        }
        else {
            throw new CustomException(ErrorCode.CHARACTERISTIC_INVALID);
        }
    }

    private Users getUserByName(UserDetails userDetails) {
        Optional<Users> users = userRepository.findByUserName(userDetails.getUsername());
        if(users.isEmpty())
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        return users.get();
    }

    private void checkEvent(Long userIdx, Long listIdx) {
        Optional<List<Event>> event = eventRepository.findAllByUserIdxAndListIdx(userIdx, listIdx);
        if(event.isEmpty() || event.get().isEmpty())
            throw new CustomException(ErrorCode.EVENT_NOT_FOUND);
    }

    private void checkUnitBehavior(Long userIdx, Long listIdx, LocalDateTime createdAt) {
        Optional<List<UnitBehavior>> behavior = behaviorRepository.findAllByUserIdxAndListIdxAndSimulationTime(userIdx, listIdx, createdAt);
        if(behavior.isEmpty() || behavior.get().isEmpty())
            throw new CustomException(ErrorCode.BEHAVIOR_NOT_FOUND);
    }

    private void checkUnitInit(Long userIdx, Long listIdx) {
        Optional<List<UnitInit>> unitInit = initRepository.findAllByUserIdxAndListIdx(userIdx, listIdx);
        if(unitInit.isEmpty() || unitInit.get().isEmpty())
            throw new CustomException(ErrorCode.UNIT_INIT_NOT_FOUND);
    }

    private void checkUnitAttributes(Long userIdx, Long listIdx) {
        Optional<List<UnitAttributes>> unitAttributes = unitRepository.findAllByUserIdxAndListIdx(userIdx, listIdx);
        if(unitAttributes.isEmpty() || unitAttributes.get().isEmpty())
            throw new CustomException(ErrorCode.UNIT_ATTRIBUTES_NOT_FOUND);
    }

    private void checkUpperAttributes(Long userIdx, Long listIdx) {
        Optional<List<UpperAttributes>> upperAttributes = upperRepository.findAllByUserIdxAndListIdx(userIdx, listIdx);
        if(upperAttributes.isEmpty() || upperAttributes.get().isEmpty())
            throw new CustomException(ErrorCode.UPPER_ATTRIBUTES_NOT_FOUND);
    }
}
