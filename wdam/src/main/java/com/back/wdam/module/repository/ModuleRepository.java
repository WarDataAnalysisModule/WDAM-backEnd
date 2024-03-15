package com.back.wdam.module.repository;

import com.back.wdam.entity.UnitBehavior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ModuleRepository extends JpaRepository<UnitBehavior, Long> {

    @Query(value = "insert into UnitBehavior (unitId, simulationTime, behaviorName, status) values (:unitId, :simulationTime, :behaviorName, :status)", nativeQuery = true)
    void save(@Param("unitId") Integer unitId, @Param("simulationTime") Integer simulationTime, @Param("behaviorName") String behaviorName, @Param("status") String status);

}
