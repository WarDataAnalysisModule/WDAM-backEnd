package com.back.wdam.module.service;

import com.back.wdam.entity.UnitBehavior;
import com.back.wdam.entity.UnitInit;
import com.back.wdam.module.dto.BehaviorDto;
import com.back.wdam.module.repository.BehaviorRepository;
import com.back.wdam.module.repository.InitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final BehaviorRepository behaviorRepository;
    private final InitRepository initRepository;

    public void behaviorSave(Integer id, BehaviorDto behaviorDto){
        Optional<UnitInit> findUnit = findByUnitId(id);

        if(!findUnit.isEmpty())
        {
            UnitInit unitInit = findUnit.get();
            behaviorRepository.saveAndFlush(new UnitBehavior(unitInit, behaviorDto));
            //behaviorRepository.saveBehavior(id, behaviorDto.getSimulationTime(),
                    //behaviorDto.getBehaviorName(), behaviorDto.getStatus());
        }

        else {
            UnitInit unitInit = new UnitInit();
            unitInit.setUnitId(id);

            unitSave(unitInit);

            behaviorRepository.saveAndFlush(new UnitBehavior(unitInit, behaviorDto));
            //behaviorRepository.saveBehavior(id, behaviorDto.getSimulationTime(),
                    //behaviorDto.getBehaviorName(), behaviorDto.getStatus());
        }

    }

    public Optional<UnitInit> findByUnitId(Integer unitId){
        return initRepository.findByUnitId(unitId);
    }

    public void unitSave(UnitInit unitInit){
        initRepository.save(unitInit);
    }
}
