package com.back.wdam.module.controller;

import com.back.wdam.module.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @GetMapping("/")
    public void fileSave(){

        String fileName = "UnitBehavior.csv";







    }
}
