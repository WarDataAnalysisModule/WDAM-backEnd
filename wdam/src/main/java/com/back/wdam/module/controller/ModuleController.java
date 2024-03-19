package com.back.wdam.module.controller;

import com.back.wdam.entity.UnitBehavior;
import com.back.wdam.entity.UnitInit;
import com.back.wdam.module.dto.BehaviorDto;
import com.back.wdam.module.service.ModuleService;
import com.fasterxml.jackson.core.util.BufferRecycler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @PostMapping("/")
    public void fileSave(@RequestPart MultipartFile file) {

        BufferedReader br = null;

        List<List<String>> lines = new ArrayList<>(); // CSV 파일의 모든 라인을 저장할 리스트

        try {
            br = new BufferedReader(new InputStreamReader(file.getInputStream(), "EUC-KR"));

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
                    Integer id = Integer.parseInt(fields.get(1));

                    BehaviorDto behaviorDto = new BehaviorDto(simulationTime.intValue(), fields.get(2), fields.get(3));
                    moduleService.behaviorSave(id, behaviorDto);

                    //moduleService.behaviorSave(id, simulationTime.intValue(), fields.get(2), fields.get(3));

                } catch (NumberFormatException e) {
                    // 정수로 변환할 수 없는 경우 예외 처리

                    System.err.println("Invalid number format in line: " + line);
                    e.printStackTrace();
                    // 예외가 발생하면 해당 라인은 스킵하고 다음 라인을 처리합니다.
                    continue;
                }

                lines.add(fields);
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
