package com.back.wdam.analyze.service;

import com.back.wdam.analyze.dto.AnalyzeResultDto;
import com.back.wdam.entity.*;
import com.back.wdam.enums.UpperUnit;
import com.back.wdam.file.repository.*;
import com.back.wdam.log.repository.LogRepository;
import com.back.wdam.user.repository.UserRepository;
import com.back.wdam.util.exception.CustomException;
import com.back.wdam.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public Long saveNewAnalyzeResult(UserDetails userDetails, String unitName, String analysisFeature, String result, String imgUrl, LocalDateTime logCreated) {

        try {
            Users user = getUserByName(userDetails);
            ResultLog resultLog = logRepository.saveAndFlush(new ResultLog(user, unitName, analysisFeature, result, imgUrl, logCreated));
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

        String preprocessedData = "", result = "", imgUrl = "";
        int exitCode;
        Users user = getUserByName(userDetails);
        String userIdx = String.valueOf(user.getUserIdx());
        String logCreatedInString = String.valueOf(logCreated);

        // module 1 실행
        try {

            InputStream in = this.getClass().getClassLoader().getResourceAsStream("pythonModule/module1.py");
            if (in == null) {
                throw new FileNotFoundException("module1.py not found in classpath");
            }
            else System.out.println("\nModule 1 found!\n");

            // module1 임시 파일 생성
            File tempFile = File.createTempFile("module1", ".py");
            Files.copy(in, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            in.close();

            // ProcessBuilder를 사용하여 파이썬 파일 실행
            ProcessBuilder processBuilder_m1;
            if(unit == null) { // 모든 유닛에 대해 분석 진행
                System.out.println("**unit x**");
                processBuilder_m1 = new ProcessBuilder(
                        "python",
                        tempFile.getAbsolutePath(),
                        userIdx,
                        logCreatedInString,
                        characteristics);
            }
            else { // 특정 유닛에 대해 분석 진행
                System.out.println("**unit o**");
                processBuilder_m1 = new ProcessBuilder(
                        "python",
                        tempFile.getAbsolutePath(),
                        userIdx,
                        logCreatedInString,
                        characteristics,
                        unit);
            }
            processBuilder_m1.redirectErrorStream(true);
            Process process_m1 = processBuilder_m1.start();

            // 프로세스 출력 읽기
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process_m1.getInputStream()))) {
                String line_m1;
                while ((line_m1 = reader.readLine()) != null) {
                    System.out.println(line_m1);
                }
            }

            exitCode = process_m1.waitFor();
            if (exitCode != 0) {
                throw new IOException("Process exited with error code " + exitCode);
            }

            // 파이썬 스크립트에서 생성한 preprocessedData.txt 파일 읽기
            Path outputFilePath = new File(System.getProperty("user.dir"), "preprocessedData.txt").toPath();
            if (Files.exists(outputFilePath)) {
                preprocessedData = Files.readString(outputFilePath);
                System.out.println("Content of preprocessedData.txt:");
                System.out.println(preprocessedData);
            } else {
                System.err.println("preprocessedData.txt not found.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.PROCESS_EXECUTION_ERROR);
        }

        if (preprocessedData.equals("")) {
            throw new CustomException(ErrorCode.PREPROCESSED_DATA_NULL);
        }

        // module 2 실행
        try {

            InputStream in = this.getClass().getClassLoader().getResourceAsStream("pythonModule/module2.py");
            if (in == null) {
                throw new FileNotFoundException("module2.py not found in classpath");
            }
            else System.out.println("\nModule 2 found!\n");

            // module2 임시 파일 생성
            File tempFile = File.createTempFile("module2", ".py");
            Files.copy(in, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            in.close();

            in = this.getClass().getClassLoader().getResourceAsStream("pythonModule/std_config.txt");
            if (in == null) {
                throw new FileNotFoundException("std_config.txt not found in classpath");
            }
            else System.out.println("\nstd_config.txt found!\n");
            
            // std_config 임시 파일 생성
            File tempStdConfig = File.createTempFile("std_config", ".txt");
            Files.copy(in, tempStdConfig.toPath(), StandardCopyOption.REPLACE_EXISTING);
            in.close();

            // ProcessBuilder를 사용하여 파이썬 파일 실행
            ProcessBuilder processBuilder_m2;
            if(unit == null) { // 모든 유닛에 대해 분석 진행
                processBuilder_m2 = new ProcessBuilder(
                        "python",
                        tempFile.getAbsolutePath(),
                        userIdx,
                        logCreatedInString,
                        characteristics,
                        preprocessedData,
                        tempStdConfig.getAbsolutePath());
            }
            else { // 특정 유닛에 대해 분석 진행
                processBuilder_m2 = new ProcessBuilder(
                        "python",
                        tempFile.getAbsolutePath(),
                        userIdx,
                        logCreatedInString,
                        characteristics,
                        preprocessedData,
                        tempStdConfig.getAbsolutePath(),
                        unit);
            }
            processBuilder_m2.redirectErrorStream(true);
            Process process_m2 = processBuilder_m2.start();

            // 프로세스 출력 읽기
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process_m2.getInputStream()))) {
                String line_m2;
                while ((line_m2 = reader.readLine()) != null) {
                    System.out.println(line_m2);
                }
            }

            exitCode = process_m2.waitFor();
            if (exitCode != 0) {
                throw new IOException("Process exited with error code " + exitCode);
            }

            // 파이썬 스크립트에서 생성한 파일 읽기
            Path resultFilePath = new File(System.getProperty("user.dir"), "result.txt").toPath();
            if (Files.exists(resultFilePath)) {
                result = Files.readString(resultFilePath);
                System.out.println("Content of result.txt:");
                System.out.println(result);
            } else {
                System.err.println("result.txt not found.");
            }

            Path imageFilePath = new File(System.getProperty("user.dir"), "img_url.txt").toPath();
            if (Files.exists(imageFilePath)) {
                imgUrl = Files.readString(imageFilePath);
                System.out.println("Content of img_url.txt:");
                System.out.println(imgUrl);
            } else {
                System.err.println("img_url.txt not found.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.PROCESS_EXECUTION_ERROR);
        }

        if (result.equals("")) {
            throw new CustomException(ErrorCode.RESULT_NOT_CREATED);
        }

        System.out.println(imgUrl);
        saveNewAnalyzeResult(userDetails, unit, characteristics, result, imgUrl, logCreated);
        return true;
    }

    public void checkDataForAnalysis(UserDetails userDetails, String characteristics, String unit, LocalDateTime logCreated) {

        System.out.println("****\n\ncheck data for analysis "+characteristics+"\n\n****");
        Users user = getUserByName(userDetails);
        // System.out.println("userIdx: " + user.getUserIdx() + " characteristics: " + characteristics + " unit: " + unit + " logCreated: " + logCreated);

        Optional<UnitList> unitList = Optional.of(new UnitList());
        if(unit != null) {

            unitList = unitListRepository.findByUserIdxAndUnitNameAndLogCreated(user.getUserIdx(), unit, logCreated);
            if(unitList.isEmpty()) {
                throw new CustomException(ErrorCode.UNIT_LIST_NOT_FOUND);
            }
        }

        if(characteristics.equals("부대 행동")) {
            System.out.println("check data for 부대 행동");
            checkUnitBehavior(user.getUserIdx(), unitList.get().getListIdx(), logCreated);
        }
        else if(characteristics.equals("부대 이동 속도 / 위치 변화")) {
            System.out.println("check data for 부대 이동 속도 / 위치 변화");
            checkUnitAttributes(user.getUserIdx(), unitList.get().getListIdx(), logCreated, false);
        }
        else if(characteristics.equals("인원/장비 수량 변화")) {
            System.out.println("check data for 인원/장비 수량 변화");
            checkUnitInit(user.getUserIdx(), unitList.get().getListIdx(), logCreated);
            checkEvent(user.getUserIdx(), unitList.get().getListIdx(), logCreated);
        }
        else if(characteristics.equals("부대의 전투력")) {
            System.out.println("check data for부대의 전투력");
            checkUnitAttributes(user.getUserIdx(), unitList.get().getListIdx(), logCreated, false);
        }
        else if(characteristics.equals("부대의 피해 상황")) {
            System.out.println("check data for부대의 피해 상황");
            checkUnitAttributes(user.getUserIdx(), unitList.get().getListIdx(), logCreated, false);
        }
        else if(characteristics.equals("개체 탐지")) {
            System.out.println("check data for개체 탐지");
            checkUnitAttributes(user.getUserIdx(), unitList.get().getListIdx(), logCreated, false);
        }
        else if(characteristics.equals("부대 정보")) {
            System.out.println("check data for 부대 정보");
            List<UnitList> unitLists = getUnitList(user.getUserIdx(), logCreated);
            for(UnitList unitFromUnitList : unitLists) {
                if(unitFromUnitList.getStatus().equals(UpperUnit.UNIT)) {
                    checkUnitInit(user.getUserIdx(), unitFromUnitList.getListIdx(), logCreated);
                }
            }
        }
        else if(characteristics.equals("부대 상태 및 지원")) {
            System.out.println("check data for 부대 상태 및 지원");
            List<UnitList> unitLists = getUnitList(user.getUserIdx(), logCreated);
            for(UnitList unitFromUnitList : unitLists) {

                boolean isUnit = checkUnitAttributes(user.getUserIdx(), unitFromUnitList.getListIdx(), logCreated, true);
                if(!isUnit) {
                    checkUpperAttributes(user.getUserIdx(), unitFromUnitList.getListIdx(), logCreated);
                }
            }
        }
        else if(characteristics.equals("부대 간 협력 분석")) {
            System.out.println("check data for 부대 간 협력 분석");
            checkUnitAttributes(user.getUserIdx(), unitList.get().getListIdx(), logCreated, false);
        }
        else if(characteristics.equals("피해 및 복구 패턴 분석")) {
            System.out.println("check data for 피해 및 복구 패턴 분석");
            checkUnitAttributes(user.getUserIdx(), unitList.get().getListIdx(), logCreated, false);
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

    private void checkEvent(Long userIdx, Long listIdx, LocalDateTime createdAt) {
        Optional<List<Event>> event = eventRepository.findAllByUserIdxAndListIdx(userIdx, listIdx, createdAt);
        if(event.isEmpty() || event.get().isEmpty())
            throw new CustomException(ErrorCode.EVENT_NOT_FOUND);
    }

    private void checkUnitBehavior(Long userIdx, Long listIdx, LocalDateTime createdAt) {
        Optional<List<UnitBehavior>> behavior = behaviorRepository.findAllByUserIdxAndListIdxAndSimulationTime(userIdx, listIdx, createdAt);
        System.out.println("userIdx: " + userIdx + " listIdx: " + listIdx + " createdAt: " + createdAt);
        if(behavior.isEmpty() || behavior.get().isEmpty()) {
            throw new CustomException(ErrorCode.BEHAVIOR_NOT_FOUND);
        }
    }

    private void checkUnitInit(Long userIdx, Long listIdx, LocalDateTime createdAt) {
        //System.out.println("userIdx: " + userIdx + " listIdx: " + listIdx + " createdAt: " + createdAt);
        Optional<UnitInit> unitInit = initRepository.findByUserIdxAndListIdx(userIdx, listIdx, createdAt);
        if(unitInit == null || unitInit.isEmpty())
            throw new CustomException(ErrorCode.UNIT_INIT_NOT_FOUND);
    }

    private boolean checkUnitAttributes(Long userIdx, Long listIdx, LocalDateTime createdAt, boolean isAll) {
        System.out.println("userIdx: " + userIdx + " listIdx: " + listIdx + " createdAt: " + createdAt + " isAll: " + isAll);
        Optional<List<UnitAttributes>> unitAttributes = unitRepository.findAllByUserIdxAndListIdx(userIdx, listIdx, createdAt);
        if (unitAttributes.isEmpty() || unitAttributes.get().isEmpty()) {
            if (!isAll) {
                throw new CustomException(ErrorCode.UNIT_ATTRIBUTES_NOT_FOUND);
            }
            return false;
        }
        return true;
    }

    private void checkUpperAttributes(Long userIdx, Long listIdx, LocalDateTime createdAt) {
        Optional<List<UpperAttributes>> upperAttributes = upperRepository.findAllByUserIdxAndListIdx(userIdx, listIdx, createdAt);
        if(upperAttributes.isEmpty() || upperAttributes.get().isEmpty()) {
            throw new CustomException(ErrorCode.UPPER_ATTRIBUTES_NOT_FOUND);
        }
    }

    private List<UnitList> getUnitList(Long userIdx, LocalDateTime createdAt) {
        List<UnitList> unitLists = new ArrayList<>();
        unitLists = unitListRepository.findAllByUserIdxAndLogCreated(userIdx, createdAt).get();
        if(unitLists == null || unitLists.isEmpty()) {
            throw new CustomException(ErrorCode.UNIT_LIST_NOT_FOUND);
        }
        return unitLists;
    }
}

