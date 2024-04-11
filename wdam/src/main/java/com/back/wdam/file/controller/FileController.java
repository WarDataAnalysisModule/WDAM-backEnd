package com.back.wdam.file.controller;

import com.back.wdam.file.dto.*;
import com.back.wdam.file.service.FileService;
import com.back.wdam.util.ApiResponse;
import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    //이 세 파일의 id는 단위부대 id
    //단위부대init_20230116174254.csv
    //단위부대Attributes_#단위부대ID_20230116174254.csv
    //Event_20230116174254.csv

    //여기 있는 id는 상급부대 id
    //UnitBehavior_20230116174254.csv
    //상급부대Attributes_#상급부대ID_20230116174254.csv

    @PostMapping("/files")
    public ResponseEntity<ApiResponse> fileSave(@RequestPart(value = "behavior") @Nullable MultipartFile behavior, //behavior 파일
                                                @RequestPart(value = "upper") @Nullable List<MultipartFile> uppers, //상급부대 정보(attributes)
                                                @RequestPart(value = "unit") @Nullable List<MultipartFile> units, //단위부대 정보(attributes)
                                                @RequestPart(value = "init") @Nullable MultipartFile init, //단위부대 정보
                                                @RequestPart(value = "event") @Nullable MultipartFile event //event 파일
                         ) throws ParseException {

        BufferedReader br = null;

        for(MultipartFile upper: uppers){

            if(!upper.isEmpty()) { //상급부대Attributes_상급부대ID_20230116174254.csv
                String unitIdString = upper.getOriginalFilename().split("_")[1]; // "_"를 기준으로 분할하여 상급부대 id를 가져옴
                unitIdString = unitIdString.replace(".csv", ""); // ".csv"를 제거
                Long unitId = Long.parseLong(unitIdString);

                String[] parts = upper.getOriginalFilename().split("_");
                String dateTimeString = parts[2].replace(".csv", "");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

                try {
                    br = new BufferedReader(new InputStreamReader(upper.getInputStream(), "EUC-KR"));

                    String line;
                    boolean isFirstLine = true; // 헤더 라인 여부를 확인하기 위한 변수

                    while ((line = br.readLine()) != null) {
                        if (isFirstLine) {
                            isFirstLine = false;
                            continue; // 첫 번째 라인(헤더)은 건너뛰고 다음 라인부터 처리
                        }

                        // 쉼표로 구분된 각 필드를 리스트에 추가
                        List<String> fields = Arrays.asList(line.split(","));

                        try {
                            Double simulationTime = Double.parseDouble(fields.get(0));
                            String unitName = fields.get(1);
                            String uuid = fields.get(2);
                            String subordinateID = fields.get(3);
                            Integer forceIdentifier = Integer.parseInt(fields.get(4));
                            String damageState = fields.get(5);

                            UpperDto upperDto = new UpperDto(simulationTime.longValue(), unitName, uuid, subordinateID, forceIdentifier,
                                    damageState, dateTime);

                            fileService.upperSave(unitId, upperDto);

                        } catch (NumberFormatException e) {
                            // 정수로 변환할 수 없는 경우 예외 처리

                            System.err.println("Invalid number format in line: " + line);
                            e.printStackTrace();
                            // 예외가 발생하면 해당 라인은 스킵하고 다음 라인을 처리합니다.
                            continue;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        for(MultipartFile unit: units) {
            if(!unit.isEmpty()) { //단위부대Attributes_단위부대ID_20230116174254.csv

                String unitIdString = unit.getOriginalFilename().split("_")[1]; // "_"를 기준으로 분할하여 단위부대 id를 가져옴
                unitIdString = unitIdString.replace(".csv", ""); // ".csv"를 제거
                Long unitId = Long.parseLong(unitIdString);

                String[] parts = unit.getOriginalFilename().split("_");
                String dateTimeString = parts[2].replace(".csv", "");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

                try {
                    br = new BufferedReader(new InputStreamReader(unit.getInputStream(), "EUC-KR"));

                    String line;
                    boolean isFirstLine = true; // 헤더 라인 여부를 확인하기 위한 변수

                    while ((line = br.readLine()) != null) {
                        if (isFirstLine) {
                            isFirstLine = false;
                            continue; // 첫 번째 라인(헤더)은 건너뛰고 다음 라인부터 처리
                        }

                        // 쉼표로 구분된 각 필드를 리스트에 추가
                        List<String> fields = Arrays.asList(line.split(","));

                        try {

                            Double simulationTime = Double.parseDouble(fields.get(0));
                            String unitName = fields.get(1);
                            String uuid = fields.get(2);
                            Double forceIdentifier = Double.parseDouble(fields.get(3));
                            String entitySymbol = fields.get(4);
                            Double positionLat = Double.parseDouble(fields.get(5));
                            Double positionOn = Double.parseDouble(fields.get(6));
                            Double positionAlt = Double.parseDouble(fields.get(7));
                            Double orientation = Double.parseDouble(fields.get(8));
                            Double speed = Double.parseDouble(fields.get(9));
                            String damageState = fields.get(10);
                            Double power = Double.parseDouble(fields.get(11));
                            String powerDistribution = fields.get(12);
                            String detectedEntityId = fields.get(13);
                            String detectedEntityDistance = fields.get(14);
                            String echelon = fields.get(15);
                            String mos = fields.get(16);

                            UnitDto unitDto = new UnitDto(simulationTime.longValue(), unitName, uuid, forceIdentifier.intValue(),
                                    entitySymbol, positionLat, positionOn, positionAlt, orientation, speed,
                                    damageState, power.intValue(), powerDistribution, detectedEntityId, detectedEntityDistance,
                                    echelon, mos, dateTime);

                            fileService.unnitSave(unitId, unitDto);

                        } catch (NumberFormatException e) {
                            // 정수로 변환할 수 없는 경우 예외 처리
                            System.err.println("Invalid number format in line: " + line);
                            e.printStackTrace();
                            // 예외가 발생하면 해당 라인은 스킵하고 다음 라인을 처리합니다.
                            continue;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if(!init.isEmpty()) { //단위부대init_20230116174254.csv

            String name = init.getOriginalFilename();
            String date = name.replaceAll("[^0-9]", ""); // 파일 이름에서 시간 부분만 추출

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime dateTime = LocalDateTime.parse(date, dateTimeFormatter);

            try {
                br = new BufferedReader(new InputStreamReader(init.getInputStream(), "EUC-KR"));

                String line;
                boolean isFirstLine = true; // 헤더 라인 여부를 확인하기 위한 변수

                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue; // 첫 번째 라인(헤더)은 건너뛰고 다음 라인부터 처리
                    }

                    // 쉼표로 구분된 각 필드를 리스트에 추가
                    List<String> fields = Arrays.asList(line.split(","));

                    try {
                        Double unitId = Double.parseDouble(fields.get(0));
                        String unitName = fields.get(1);
                        String symbol = fields.get(2);
                        String status =fields.get(3);
                        String member = fields.get(4);
                        String equipment = fields.get(5);
                        String supply = fields.get(6);

                        InitDto initDto = new InitDto(unitName, symbol, status, member, equipment, supply, dateTime);

                        System.out.println(initDto);

                        fileService.initSave(unitId.longValue(), initDto);

                    } catch (NumberFormatException e) {
                        // 정수로 변환할 수 없는 경우 예외 처리

                        System.err.println("Invalid number format in line: " + line);
                        e.printStackTrace();
                        // 예외가 발생하면 해당 라인은 스킵하고 다음 라인을 처리합니다.
                        continue;
                    }
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if(!behavior.isEmpty()){
            String fileName = behavior.getOriginalFilename();
            String datePart = fileName.replaceAll("[^0-9]", ""); // 파일 이름에서 시간 부분만 추출

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime dateTime = LocalDateTime.parse(datePart, formatter);

            try {
                br = new BufferedReader(new InputStreamReader(behavior.getInputStream(), "EUC-KR"));

                String line;
                boolean isFirstLine = true; // 헤더 라인 여부를 확인하기 위한 변수

                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue; // 첫 번째 라인(헤더)은 건너뛰고 다음 라인부터 처리
                    }

                    // 쉼표로 구분된 각 필드를 리스트에 추가
                    List<String> fields = Arrays.asList(line.split(","));

                    try {
                        Double simulationTime = Double.parseDouble(fields.get(0));
                        Long id = Long.parseLong(fields.get(1));

                        BehaviorDto behaviorDto = new BehaviorDto(simulationTime.longValue(), fields.get(2), fields.get(3), dateTime);
                        fileService.behaviorSave(id, behaviorDto);

                    } catch (NumberFormatException e) {
                        // 정수로 변환할 수 없는 경우 예외 처리

                        System.err.println("Invalid number format in line: " + line);
                        e.printStackTrace();
                        // 예외가 발생하면 해당 라인은 스킵하고 다음 라인을 처리합니다.
                        continue;
                    }
                }
            } catch (IOException e) {
                // 파일을 읽거나 닫을 때 발생할 수 있는 예외 처리
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) br.close(); // BufferedReader 닫기
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(!event.isEmpty()){ //Event_20230116174254.csv
            String fileName = event.getOriginalFilename();
            String datePart = fileName.replaceAll("[^0-9]", ""); // 파일 이름에서 시간 부분만 추출

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime dateTime = LocalDateTime.parse(datePart, formatter);

            try {
                br = new BufferedReader(new InputStreamReader(event.getInputStream(), "EUC-KR"));

                String line;
                boolean isFirstLine = true; // 헤더 라인 여부를 확인하기 위한 변수

                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue; // 첫 번째 라인(헤더)은 건너뛰고 다음 라인부터 처리
                    }

                    // 쉼표로 구분된 각 필드를 리스트에 추가
                    List<String> fields = Arrays.asList(line.split(","));

                    try {

                        Double simulationTime = Double.parseDouble(fields.get(0));
                        Long sourceId = Long.parseLong(fields.get(1));

                        if(fields.get(3).equals("CloseCombat")){
                            // 정규 표현식 패턴: 숫자 패턴
                            Pattern pattern = Pattern.compile("\\d+");

                            // 입력 문자열에 대한 Matcher 생성
                            Matcher matcher = pattern.matcher(fields.get(2));

                            // 숫자를 추출하여 출력
                            while (matcher.find()) {
                                String numberStr = matcher.group(); // 매칭된 숫자 문자열
                                Long targetId = Long.parseLong(numberStr); // 문자열을 정수로 변환

                                Double distance;
                                if(fields.get(10).equals("-")) distance = 0.0;
                                else distance = Double.parseDouble(fields.get(10));

                                EventDto eventDto = new EventDto(simulationTime.longValue(), fields.get(3), fields.get(4), fields.get(5),
                                        fields.get(6), fields.get(7), fields.get(8), fields.get(9), distance.intValue(),
                                        fields.get(11), fields.get(12), dateTime);
                                fileService.eventSave(sourceId, targetId, eventDto);
                            }
                        }
                        else{
                            Long targetId = Long.parseLong(fields.get(2));
                            Double distance = Double.parseDouble(fields.get(10));

                            EventDto eventDto = new EventDto(simulationTime.longValue(), fields.get(3), fields.get(4), fields.get(5),
                                    fields.get(6), fields.get(7), fields.get(8), fields.get(9), distance.intValue(),
                                    fields.get(11), fields.get(12), dateTime);
                            fileService.eventSave(sourceId, targetId, eventDto);
                        }

                    } catch (NumberFormatException e) {
                        // 정수로 변환할 수 없는 경우 예외 처리

                        System.err.println("Invalid number format in line: " + line);
                        e.printStackTrace();
                        // 예외가 발생하면 해당 라인은 스킵하고 다음 라인을 처리합니다.
                        continue;
                    }
                }
            } catch (IOException e) {
                // 파일을 읽거나 닫을 때 발생할 수 있는 예외 처리
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) br.close(); // BufferedReader 닫기
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
