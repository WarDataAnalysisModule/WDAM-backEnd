package com.back.wdam.file.repository;

import com.back.wdam.entity.UnitAttributes;
import com.back.wdam.entity.UnitBehavior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UnitRepository extends JpaRepository<UnitAttributes, Long> {

    @Query("SELECT u FROM UnitAttributes u WHERE u.users.userIdx = :userIdx AND u.unitList.listIdx = :listIdx")
    Optional<UnitAttributes> findByUserIdxAndListIdx(@Param("userIdx") Long userIdx, @Param("listIdx") Long listIdx);
}
