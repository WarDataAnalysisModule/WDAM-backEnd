package com.back.wdam.file.service;

import com.back.wdam.entity.*;
import com.back.wdam.enums.UpperUnit;
import com.back.wdam.file.dto.*;
import com.back.wdam.file.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {

    private final BehaviorRepository behaviorRepository;
    private final InitRepository initRepository;
    private final UnitListRepository unitListRepository;
    private final UpperRepository upperRepository;
    private final UnitRepository unitRepository;
    private final EventRepository eventRepository;


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
            if(unitList.get().getUnitName()==null){
                unitListRepository.updateByUnitId(upperDto.getUnitName(), String.valueOf(UpperUnit.UPPER), id);
            }
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
            if(unitList.get().getUnitName()==null){
                unitListRepository.updateByUnitId(unitDto.getUnitName(), String.valueOf(UpperUnit.UNIT), id);
            }
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
            if(unitList.get().getUnitName()==null){
                unitListRepository.updateByUnitId(initDto.getUnitName(), String.valueOf(UpperUnit.UNIT), id);
            }
            initRepository.saveAndFlush(new UnitInit(unitList.get(), initDto));
        }
    }

    public void behaviorSave(Long id, BehaviorDto behaviorDto){
        Optional<UnitList> unitList = unitListRepository.findByUnitId(id);
        /*if(unitList.isEmpty()){
            UnitList newUnit = new UnitList();
            newUnit.setUnitId(id);
            newUnit.setStatus(UpperUnit.UNIT);
            unitListRepository.saveAndFlush(newUnit);

            behaviorRepository.saveAndFlush(new UnitBehavior(newUnit, behaviorDto));
        }
        else{
            behaviorRepository.saveAndFlush(new UnitBehavior(unitList.get(), behaviorDto));
        }*/
        behaviorRepository.saveAndFlush(new UnitBehavior(unitList.get(), behaviorDto));
    }

    public void eventSave(Long source, Long target, EventDto eventDto){
        Optional<UnitList> sourceUnit = unitListRepository.findByUnitId(source);
        Optional<UnitList> targetUnit = unitListRepository.findByUnitId(target);

        /*if(sourceUnit.isEmpty()){
            UnitList newUnit = new UnitList();
            newUnit.setUnitId(source);
            newUnit.setStatus(UpperUnit.UNIT);
            unitListRepository.saveAndFlush(newUnit);

            sourceUnit = unitListRepository.findByUnitId(source);
        }

        if(targetUnit.isEmpty()){
            UnitList newUnit = new UnitList();
            newUnit.setUnitId(target);
            newUnit.setStatus(UpperUnit.UNIT);
            unitListRepository.saveAndFlush(newUnit);

            targetUnit = unitListRepository.findByUnitId(target);
        }*/

        eventRepository.saveAndFlush(new Event(sourceUnit.get(), targetUnit.get(), eventDto));

    }
    public List<UnitListDto> getUnitList(){
        List<UnitList> unitLists = unitListRepository.findAll();
        List<UnitListDto> unitListDtos = new ArrayList<>();
        for(UnitList unitList: unitLists){
            UnitListDto unitListDto = new UnitListDto(unitList.getListIdx(), unitList.getUnitName());
            unitListDtos.add(unitListDto);
        }
        return unitListDtos;
    }

}
