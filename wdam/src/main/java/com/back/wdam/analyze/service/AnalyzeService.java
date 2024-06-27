package com.back.wdam.analyze.service;

import com.back.wdam.analyze.dto.AnalyzeResultDto;
import com.back.wdam.entity.*;
import com.back.wdam.file.repository.*;
import com.back.wdam.log.repository.LogRepository;
import com.back.wdam.user.repository.UserRepository;
import com.back.wdam.util.exception.CustomException;
import com.back.wdam.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

            InputStream in = this.getClass().getClassLoader().getResourceAsStream("pythonModule/module1.py");
            if (in == null) {
                throw new FileNotFoundException("module1.py not found in classpath");
            }
            else System.out.println("\nModule 1 found!\n");

            // 임시 파일 생성
            File tempFile = File.createTempFile("module1", ".py");
            Files.copy(in, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            in.close();

            // ProcessBuilder를 사용하여 파이썬 파일 실행
            ProcessBuilder processBuilder_m1 = new ProcessBuilder("python", tempFile.getAbsolutePath(), characteristics, unit);
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

            // 파이썬 스크립트에서 생성한 파일 읽기
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

        try {

            InputStream in = this.getClass().getClassLoader().getResourceAsStream("pythonModule/module2.py");
            if (in == null) {
                throw new FileNotFoundException("module2.py not found in classpath");
            }
            else System.out.println("\nModule 2 found!\n");

            // 임시 파일 생성
            File tempFile = File.createTempFile("module2", ".py");
            Files.copy(in, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            in.close();

            // ProcessBuilder를 사용하여 파이썬 파일 실행
            ProcessBuilder processBuilder_m2 = new ProcessBuilder("python", tempFile.getAbsolutePath(), characteristics, unit, preprocessedData);
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
            Path outputFilePath = new File(System.getProperty("user.dir"), "result.txt").toPath();
            if (Files.exists(outputFilePath)) {
                result = Files.readString(outputFilePath);
                System.out.println("Content of result.txt:");
                System.out.println(result);
            } else {
                System.err.println("result.txt not found.");
            }
//            InputStream in = this.getClass().getClassLoader().getResourceAsStream("pythonModule/module2.py");
//            if (in == null) {
//                throw new FileNotFoundException("module2.py not found in classpath");
//            }
//            else System.out.println("\nModule 2 found!\n");
//
////            File tempFile2 = File.createTempFile("module2", ".py");
////            Files.copy(in, tempFile2.toPath(), StandardCopyOption.REPLACE_EXISTING);
////            in.close();
//
//            processBuilder = new ProcessBuilder("python"
//                    , characteristics
//                    , unit
//                    , preprocessedData);
//            process = processBuilder.start();
//
//
////            // module2 실행
////            processBuilder = new ProcessBuilder("python"
////                    , "src\\main\\java\\com\\back\\wdam\\pythonModule\\module2.py"
////                    , characteristics
////                    , unit
////                    , preprocessedData);
////            process = processBuilder.start();
//
//            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
//            while ((line = stderrReader.readLine()) != null) {
//                System.out.println("Error: " + line);
//            }
//
//            exitCode = process.waitFor();
//            if (exitCode != 0) {
//                throw new IOException("Process exited with error code " + exitCode);
//            }
//
////            // Read the result from the temp file
////            File resultFile = new File("src/main/java/com/back/wdam/analyze/resources/result.txt");
////            try (BufferedReader fileReader = new BufferedReader(new FileReader(resultFile))) {
////                StringBuilder outputBuilder = new StringBuilder();
////                while ((line = fileReader.readLine()) != null) {
////                    outputBuilder.append(line).append("\n");
////                }
////                result = outputBuilder.toString().trim();
//
//            StringBuilder outputBuilder = new StringBuilder();
//            try (BufferedReader fileReader = new BufferedReader(new FileReader("src\\main\\java\\com\\back\\wdam\\analyze\\resources\\result.txt"))) {
//                while ((line = fileReader.readLine()) != null) {
//                    outputBuilder.append(line).append("\n");
//                }
//                result = outputBuilder.toString().trim();

//            } catch (IOException e) {
//                e.printStackTrace();
//            }

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
