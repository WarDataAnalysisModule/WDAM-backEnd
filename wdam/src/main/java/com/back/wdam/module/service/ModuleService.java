package com.back.wdam.module.service;

import com.back.wdam.module.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public void save(Integer unitId, Integer simulationTime, String behaviorName, String status){
        moduleRepository.save(unitId, simulationTime, behaviorName, status);
    }
}
