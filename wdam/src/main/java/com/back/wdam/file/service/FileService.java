package com.back.wdam.file.service;

import com.back.wdam.entity.*;
import com.back.wdam.enums.UpperUnit;
import com.back.wdam.file.dto.*;
import com.back.wdam.file.repository.*;
import com.back.wdam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FileService {

    private final BehaviorRepository behaviorRepository;
    private final InitRepository initRepository;
    private final UnitListRepository unitListRepository;
    private final UpperRepository upperRepository;
    private final UnitRepository unitRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;


    public void upperSave(Long id, UpperDto upperDto, UserDetails userDetails, LocalDateTime dateTime){

        Long userIdx = userRepository.findByUserName(userDetails.getUsername()).get().getUserIdx();
        Optional<UnitList> unitList = unitListRepository.findByUnitId(id, userIdx, dateTime);

        if(unitList.isEmpty()){
            UnitList newUnit = new UnitList();
            newUnit.setUnitId(id);
            newUnit.setUnitName(upperDto.getUnitName());
            newUnit.setStatus(UpperUnit.UPPER);
            newUnit.setSimulationTime(dateTime);
            newUnit.setUsers(userRepository.findByUserName(userDetails.getUsername()).get());
            unitListRepository.saveAndFlush(newUnit);

            upperRepository.saveAndFlush(new UpperAttributes(newUnit, upperDto));
        }
        else{
            if(unitList.get().getUnitName().contains("데이터 부재")){ //simulationTime column도 비교해야 함
                //simulationTime이 달라지면 unitList의 unitId가 같아도 unitName아 다를 수 있지 않나? - 다른 전투느낌으로
                //근데 simulationTime이 초까지 나와 있는데, 어느 정도까지 다르다고 봐야 아예 다른 전투(?)라고 봐야 하나
                unitListRepository.updateByUnitId(upperDto.getUnitName(), String.valueOf(UpperUnit.UPPER), id);
                unitList = unitListRepository.findByUnitId(id, userIdx, dateTime);
            }
            upperRepository.saveAndFlush(new UpperAttributes(unitList.get(), upperDto));
        }
    }

    public void unnitSave(Long id, UnitDto unitDto, UserDetails userDetails, LocalDateTime dateTime){
        Long userIdx = userRepository.findByUserName(userDetails.getUsername()).get().getUserIdx();
        Optional<UnitList> unitList = unitListRepository.findByUnitId(id, userIdx, dateTime);

        if(unitList.isEmpty()){
            UnitList newUnit = new UnitList();
            newUnit.setUnitId(id);
            newUnit.setUsers(userRepository.findByUserName(userDetails.getUsername()).get());
            newUnit.setUnitName(unitDto.getUnitName());
            newUnit.setSimulationTime(dateTime);
            newUnit.setStatus(UpperUnit.UNIT);

            unitListRepository.saveAndFlush(newUnit);

            unitRepository.saveAndFlush(new UnitAttributes(newUnit, unitDto));
        }
        else{
            if(unitList.get().getUnitName().contains("데이터 부재")){
                unitListRepository.updateByUnitId(unitDto.getUnitName(), String.valueOf(UpperUnit.UNIT), id);
                unitList = unitListRepository.findByUnitId(id, userIdx, dateTime);
            }
            unitRepository.saveAndFlush(new UnitAttributes(unitList.get(), unitDto));
        }
    }

    public void initSave(Long id, InitDto initDto, UserDetails userDetails, LocalDateTime dateTime){
        Long userIdx = userRepository.findByUserName(userDetails.getUsername()).get().getUserIdx();
        Optional<UnitList> unitList = unitListRepository.findByUnitId(id, userIdx, dateTime);

        if(unitList.isEmpty()){
            UnitList newUnit = new UnitList();
            newUnit.setUnitId(id);
            newUnit.setUsers(userRepository.findByUserName(userDetails.getUsername()).get());
            newUnit.setUnitName(initDto.getUnitName());
            newUnit.setSimulationTime(dateTime);
            newUnit.setStatus(UpperUnit.UNIT);

            unitListRepository.saveAndFlush(newUnit);

            initRepository.saveAndFlush(new UnitInit(newUnit, initDto));
        }
        else{
            if(unitList.get().getUnitName().contains("데이터 부재")){
                unitListRepository.updateByUnitId(initDto.getUnitName(), String.valueOf(UpperUnit.UNIT), id);
                unitList = unitListRepository.findByUnitId(id, userIdx, dateTime);
            }
            initRepository.saveAndFlush(new UnitInit(unitList.get(), initDto));
        }
    }

    public void behaviorSave(Long id, BehaviorDto behaviorDto, UserDetails userDetails, LocalDateTime dateTime){
        Long userIdx = userRepository.findByUserName(userDetails.getUsername()).get().getUserIdx();
        Optional<UnitList> unitList = unitListRepository.findByUnitId(id, userIdx, dateTime);

        if(unitList.isEmpty()){
            UnitList newUnit = new UnitList();
            newUnit.setUnitId(id);
            newUnit.setUnitName("데이터 부재(id: "+id+")");
            newUnit.setSimulationTime(dateTime);
            newUnit.setUsers(userRepository.findByUserName(userDetails.getUsername()).get());
            unitListRepository.saveAndFlush(newUnit);
            newUnit = unitListRepository.findByUnitId(id, userIdx, dateTime).get();
            behaviorRepository.saveAndFlush(new UnitBehavior(newUnit, behaviorDto));
        }
        else{
            behaviorRepository.saveAndFlush(new UnitBehavior(unitList.get(), behaviorDto));
        }
        //behaviorRepository.saveAndFlush(new UnitBehavior(unitList.get(), behaviorDto));
    }

    public void eventSave(Long source, Long target, EventDto eventDto, UserDetails userDetails, LocalDateTime dateTime){
        Long userIdx = userRepository.findByUserName(userDetails.getUsername()).get().getUserIdx();
        Optional<UnitList> sourceUnit = unitListRepository.findByUnitId(source, userIdx, dateTime);
        Optional<UnitList> targetUnit = unitListRepository.findByUnitId(target, userIdx, dateTime);

        if(sourceUnit.isEmpty()){
            UnitList newUnit = new UnitList();
            newUnit.setUnitId(source);
            newUnit.setUnitName("데이터 부재(id: "+source+")");
            newUnit.setSimulationTime(dateTime);
            newUnit.setUsers(userRepository.findByUserName(userDetails.getUsername()).get());
//            newUnit.setStatus(UpperUnit.UNIT);
            unitListRepository.saveAndFlush(newUnit);
            sourceUnit = unitListRepository.findByUnitId(source, userIdx, dateTime);
        }

        if(targetUnit.isEmpty()){
            UnitList newUnit = new UnitList();
            newUnit.setUnitId(target);
            newUnit.setUnitName("데이터 부재(id: "+target+")");
            newUnit.setSimulationTime(dateTime);
            newUnit.setUsers(userRepository.findByUserName(userDetails.getUsername()).get());
//            newUnit.setStatus(UpperUnit.UNIT);
            unitListRepository.saveAndFlush(newUnit);
            targetUnit = unitListRepository.findByUnitId(target, userIdx, dateTime);
        }
        eventRepository.saveAndFlush(new Event(sourceUnit.get(), targetUnit.get(), eventDto));

    }
//    public List<UnitListDto> getUnitList(){
//        List<UnitList> unitLists = unitListRepository.findAll();
//        List<UnitListDto> unitListDtos = new ArrayList<>();
//        for(UnitList unitList: unitLists){
//            UnitListDto unitListDto = new UnitListDto(unitList.getListIdx(), unitList.getUnitName());
//            unitListDtos.add(unitListDto);
//        }
//        return unitListDtos;
//    }

    public Set<LocalDateTime> getSimulationTimes(UserDetails userDetails, Set<LocalDateTime> simulationTimes){

        Users user = userRepository.findByUserName(userDetails.getUsername()).get();
        List<UpperAttributes> attributes = upperRepository.findByUsers(user);
        for(UpperAttributes attribute: attributes){
            simulationTimes.add(attribute.getCreatedAt());
        }

        List<UnitAttributes> unitAttributes = unitRepository.findByUsers(user);
        for(UnitAttributes attribute: unitAttributes){
            simulationTimes.add(attribute.getCreatedAt());
        }

        List<UnitInit> unitInits = initRepository.findByUsers(user);
        for(UnitInit unitInit: unitInits){
            simulationTimes.add(unitInit.getCreatedAt());
        }

        List<UnitBehavior> unitBehaviors = behaviorRepository.findByUsers(user);
        for(UnitBehavior behavior: unitBehaviors){
            simulationTimes.add(behavior.getCreatedAt());
        }

        List<Event> events = eventRepository.findByUsers(user);
        for(Event event: events){
            simulationTimes.add(event.getCreatedAt());
        }

        return simulationTimes;
    }

}
