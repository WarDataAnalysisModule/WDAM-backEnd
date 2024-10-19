package com.back.wdam.file.repository;

import com.back.wdam.entity.UnitAttributes;
import com.back.wdam.entity.UpperAttributes;
import com.back.wdam.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import java.util.List;

public interface UnitRepository extends JpaRepository<UnitAttributes, Long> {
    List<UnitAttributes> findByUsers(Users user);

    @Query("SELECT u FROM UnitAttributes u WHERE u.users.userIdx = :userIdx AND u.unitList.listIdx = :listIdx AND u.createdAt = :createdAt")
    Optional<List<UnitAttributes>> findAllByUserIdxAndListIdx(@Param("userIdx") Long userIdx, @Param("listIdx") Long listIdx, @Param("createdAt") LocalDateTime createdAt);

    @Query("SELECT u FROM UnitAttributes u WHERE u.unitList.unitId = :id AND u.users.userIdx = :userIdx AND u.createdAt = :createdAt")
    List<UnitAttributes> findAllByUserIdxAndCreatedAt(@Param("id") Long id, @Param("userIdx") Long userIdx, @Param("createdAt") LocalDateTime createdAt);
}
