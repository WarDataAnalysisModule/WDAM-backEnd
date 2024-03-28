package com.back.wdam.module.controller;

import com.back.wdam.entity.UnitBehavior;
import com.back.wdam.entity.UnitInit;
import com.back.wdam.module.dto.BehaviorDto;
import com.back.wdam.module.dto.InitDto;
import com.back.wdam.module.dto.UnitDto;
import com.back.wdam.module.dto.UpperDto;
import com.back.wdam.module.service.ModuleService;
import com.fasterxml.jackson.core.util.BufferRecycler;
import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    //이 세 파일의 id는 단위부대 id
    //단위부대init_20230116174254.csv
    //단위부대Attributes_#단위부대ID_20230116174254.csv
    //Event_20230116174254.csv

    //여기 있는 id는 상급부대 id
    //UnitBehavior_20230116174254.csv
    //상급부대Attributes_#상급부대ID_20230116174254.csv

    @PostMapping("/files")
    public void fileSave(@RequestPart(value = "behavior") @Nullable MultipartFile behavior,
                         @RequestPart(value = "upper") @Nullable MultipartFile upper, //상급부대 정보(attributes)
                         @RequestPart(value = "unit") @Nullable MultipartFile unit, //단위부대 정보(attributes)
                         @RequestPart(value = "init") @Nullable MultipartFile init //단위부대 정보
                         ) throws ParseException {

        BufferedReader br = null;

        if(upper!=null) { //상급부대Attributes_상급부대ID_20230116174254.csv
            String unitIdString = upper.getOriginalFilename().split("_")[1]; // "_"를 기준으로 분할하여 상급부대1 id를 가져옴
            unitIdString = unitIdString.replace(".csv", ""); // ".csv"를 제거
            Long unitId = Long.parseLong(unitIdString);
            //System.out.println("상급부대 ID: " + unitId);

            String[] parts = upper.getOriginalFilename().split("_");
            String dateTimeString = parts[2].replace(".csv", "");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
            //System.out.println("Extracted Date/Time: " + dateTime);


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

                        moduleService.upperSave(unitId, upperDto);

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

        if(unit!=null) { //단위부대Attributes_단위부대ID_20230116174254.csv

            String unitIdString = unit.getOriginalFilename().split("_")[1]; // "_"를 기준으로 분할하여 상급부대1 id를 가져옴
            unitIdString = unitIdString.replace(".csv", ""); // ".csv"를 제거
            Long unitId = Long.parseLong(unitIdString);
            //System.out.println("상급부대 ID: " + unitId);

            String[] parts = upper.getOriginalFilename().split("_");
            String dateTimeString = parts[2].replace(".csv", "");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
            //System.out.println("Extracted Date/Time: " + dateTime);

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

                        moduleService.unnitSave(unitId, unitDto);

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

        if(init!=null) { //단위부대init_20230116174254.csv
            String name = init.getOriginalFilename();
            String date = name.replaceAll("[^0-9]", ""); // 파일 이름에서 시간 부분만 추출

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime dateTime = LocalDateTime.parse(date, dateTimeFormatter);

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
                        Double unitId = Double.parseDouble(fields.get(0));
                        String unitName = fields.get(1);
                        String symbol = fields.get(2);
                        String status =fields.get(3);
                        String member = fields.get(4);
                        String equipment = fields.get(5);
                        String supply = fields.get(6);

                        InitDto initDto = new InitDto(unitName, symbol, status, member, equipment, supply, dateTime);

                        moduleService.initSave(unitId.longValue(), initDto);

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
                    moduleService.behaviorSave(id, behaviorDto);

                    //moduleService.behaviorSave(id, simulationTime.intValue(), fields.get(2), fields.get(3));

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
}
