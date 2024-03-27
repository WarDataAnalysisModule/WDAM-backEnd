package com.back.wdam.module.service;

import com.back.wdam.entity.*;
import com.back.wdam.enums.UpperUnit;
import com.back.wdam.module.dto.BehaviorDto;
import com.back.wdam.module.dto.InitDto;
import com.back.wdam.module.dto.UnitDto;
import com.back.wdam.module.dto.UpperDto;
import com.back.wdam.module.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final BehaviorRepository behaviorRepository;
    private final InitRepository initRepository;
    private final UnitListRepository unitListRepository;
    private final UpperRepository upperRepository;
    private final UnitRepository unitRepository;


    public void upperSave(Long id, UpperDto upperDto){
        Optional<UnitList> unitList = unitListRepository.findByUnitId(id);

        if(unitList.isEmpty()){
            UnitList newUnit = new UnitList();
            newUnit.setUnitId(id);
            newUnit.setUnitName(upperDto.getUnitName());
            newUnit.setStatus(UpperUnit.UPPER);

            unitListRepository.saveAndFlush(newUnit);

            upperRepository.saveAndFlush(new UpperAttributes(newUnit, upperDto));
        }
        else{
            upperRepository.saveAndFlush(new UpperAttributes(unitList.get(), upperDto));
        }
    }

    public void unnitSave(Long id, UnitDto unitDto){
        Optional<UnitList> unitList = unitListRepository.findByUnitId(id);

        if(unitList.isEmpty()){
            UnitList newUnit = new UnitList();
            newUnit.setUnitId(id);
            newUnit.setUnitName(unitDto.getUnitName());
            newUnit.setStatus(UpperUnit.UNIT);

            unitListRepository.saveAndFlush(newUnit);

            unitRepository.saveAndFlush(new UnitAttributes(newUnit, unitDto));
        }
        else{
            unitRepository.saveAndFlush(new UnitAttributes(unitList.get(), unitDto));
        }
    }

    public void initSave(Long id, InitDto initDto){
        Optional<UnitList> unitList = unitListRepository.findByUnitId(id);

        if(unitList.isEmpty()){
            UnitList newUnit = new UnitList();
            newUnit.setUnitId(id);
            newUnit.setUnitName(initDto.getUnitName());
            newUnit.setStatus(UpperUnit.UNIT);

            unitListRepository.saveAndFlush(newUnit);

            initRepository.saveAndFlush(new UnitInit(newUnit, initDto));
        }
        else{
            initRepository.saveAndFlush(new UnitInit(unitList.get(), initDto));
        }
    }

    public void behaviorSave(Long id, BehaviorDto behaviorDto){
        Optional<UnitList> unitList = unitListRepository.findByUnitId(id);
        if(!unitList.isEmpty()){
            behaviorRepository.saveAndFlush(new UnitBehavior(unitList.get(), behaviorDto));
        }
    }

}
