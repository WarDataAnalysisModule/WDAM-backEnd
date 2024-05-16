package com.back.wdam.file.repository;

import com.back.wdam.entity.UnitAttributes;
import com.back.wdam.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import java.util.List;

public interface UnitRepository extends JpaRepository<UnitAttributes, Long> {
    List<UnitAttributes> findByUsers(Users user);

    @Query("SELECT u FROM UnitAttributes u WHERE u.users.userIdx = :userIdx AND u.unitList.listIdx = :listIdx")
    Optional<UnitAttributes> findByUserIdxAndListIdx(@Param("userIdx") Long userIdx, @Param("listIdx") Long listIdx);
}
