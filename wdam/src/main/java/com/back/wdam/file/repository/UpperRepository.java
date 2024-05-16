package com.back.wdam.file.repository;

import com.back.wdam.entity.UnitAttributes;
import com.back.wdam.entity.UpperAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UpperRepository extends JpaRepository<UpperAttributes, Long> {

    @Query("SELECT u FROM UpperAttributes u WHERE u.users.userIdx = :userIdx AND u.unitList.listIdx = :listIdx")
    Optional<UpperAttributes> findByUserIdxAndListIdx(@Param("userIdx") Long userIdx, @Param("listIdx") Long listIdx);
}
