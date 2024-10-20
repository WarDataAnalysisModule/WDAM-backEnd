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
import org.springframework.transaction.annotation.Transactional;

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

    public boolean upperCheck(Long id, Long userIdx, LocalDateTime simulationTime){
        return !upperRepository.findAllByUserIdxAndCreatedAt(id, userIdx, simulationTime).isEmpty();
    }


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
            if(unitList.get().getUnitName().contains("데이터 부재")){
                unitListRepository.updateByUnitId(upperDto.getUnitName(), UpperUnit.UPPER, id);
                unitList = unitListRepository.findByUnitId(id, userIdx, dateTime);
            }
            upperRepository.saveAndFlush(new UpperAttributes(unitList.get(), upperDto));
        }
    }

    public boolean unitCheck(Long id, Long userIdx, LocalDateTime simulationTime){
        return !unitRepository.findAllByUserIdxAndCreatedAt(id, userIdx, simulationTime).isEmpty();
    }

    public void unitSave(Long id, UnitDto unitDto, UserDetails userDetails, LocalDateTime dateTime){
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
                unitListRepository.updateByUnitId(unitDto.getUnitName(), UpperUnit.UNIT, id);
                unitList = unitListRepository.findByUnitId(id, userIdx, dateTime);
            }
            unitRepository.saveAndFlush(new UnitAttributes(unitList.get(), unitDto));
        }
    }

    public boolean initCheck(Long userIdx, LocalDateTime simulationTime){
        return !initRepository.findAllByUserIdxAndCreatedAt(userIdx, simulationTime).isEmpty();
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
                unitListRepository.updateByUnitId(initDto.getUnitName(), UpperUnit.UNIT, id);
                unitList = unitListRepository.findByUnitId(id, userIdx, dateTime);
            }
            initRepository.saveAndFlush(new UnitInit(unitList.get(), initDto));
        }
    }

    public boolean behaviorCheck(Long userIdx, LocalDateTime simulationTime){
        return !behaviorRepository.findAllByUserIdxAndCreatedAt(userIdx, simulationTime).isEmpty();
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

    public boolean eventCheck(Long userIdx, LocalDateTime simulationTime){
        return !eventRepository.findAllByUserIdxAndCreatedAt(userIdx, simulationTime).isEmpty();
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
