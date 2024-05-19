package com.back.wdam.file.repository;

import com.back.wdam.entity.UnitList;
import com.back.wdam.enums.UpperUnit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
public interface UnitListRepository extends JpaRepository<UnitList, Long> {
    @Query("select u from UnitList u where u.unitId = :unitId and u.users.userIdx = :userIdx and u.simulationTime = :simulationTime")
    Optional<UnitList> findByUnitId(@Param("unitId") Long unitId, @Param("userIdx") Long userIdx, @Param("simulationTime")LocalDateTime simulationTime);

    @Modifying
    @Query("update UnitList u set u.unitName = :unitName, u.status = :status where u.unitId = :unitId")
    void updateByUnitId(@Param("unitName") String unitName, @Param("status") UpperUnit status, @Param("unitId") Long unitId);

    @Query("SELECT u FROM UnitList u")
    List<UnitList> findAll();

    @Query("SELECT u FROM UnitList u WHERE u.users.userIdx = :userIdx AND u.unitName = :unitName")
    Optional<List<UnitList>> findAllByUserIdxAndUnitName(@Param("userIdx") Long userId, @Param("unitName") String unitName);

    @Query("select u from UnitList u where u.users.userIdx = :userIdx and u.simulationTime = :simulationTime")
    Optional<List<UnitList>> findAllByUserIdxAndLogCreated(@Param("userIdx") Long userIdx, @Param("simulationTime")LocalDateTime simulationTime);
}
