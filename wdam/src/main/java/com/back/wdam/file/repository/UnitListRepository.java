package com.back.wdam.file.repository;

import com.back.wdam.entity.UnitList;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Transactional
public interface UnitListRepository extends JpaRepository<UnitList, Long> {
    Optional<UnitList> findByUnitId(Long unitList);

    @Modifying
    @Query("update UnitList u set u.unitName = :unitName, u.status = :status where u.unitId = :unitId")
    void updateByUnitId(@Param("unitName") String unitName, @Param("status") String status, @Param("unitId") Long unitId);

}
