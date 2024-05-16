package com.back.wdam.file.repository;

import com.back.wdam.entity.UnitBehavior;
import com.back.wdam.entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BehaviorRepository extends JpaRepository<UnitBehavior, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO unit_behavior (unit_id, simulation_time, behavior_name, status, created_at) VALUES (:unitId, :simulationTime, :behaviorName, :status, :createdAt)", nativeQuery = true)
    void saveBehavior(@Param("unitId") Integer unitId, @Param("simulationTime") Integer simulationTime, @Param("behaviorName") String behaviorName, @Param("status") String status, @Param("createdAt")LocalDateTime createdAt);

    List<UnitBehavior> findByUsers(Users user);
    @Query("SELECT b FROM UnitBehavior b WHERE b.users.userIdx = :userIdx AND b.unitList.listIdx = :listIdx")
    Optional<UnitBehavior> findByUserIdxAndListIdx(@Param("userIdx") Long userIdx, @Param("listIdx") Long listIdx);
}
