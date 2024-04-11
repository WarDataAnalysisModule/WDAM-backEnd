package com.back.wdam.file.repository;

import com.back.wdam.entity.UnitBehavior;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface BehaviorRepository extends JpaRepository<UnitBehavior, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO unit_behavior (unit_id, simulation_time, behavior_name, status, created_at) VALUES (:unitId, :simulationTime, :behaviorName, :status, :createdAt)", nativeQuery = true)
    void saveBehavior(@Param("unitId") Integer unitId, @Param("simulationTime") Integer simulationTime, @Param("behaviorName") String behaviorName, @Param("status") String status, @Param("createdAt")LocalDateTime createdAt);
}
