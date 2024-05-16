package com.back.wdam.file.repository;

import com.back.wdam.entity.UpperAttributes;
import com.back.wdam.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import java.util.List;

public interface UpperRepository extends JpaRepository<UpperAttributes, Long> {

    List<UpperAttributes> findByUsers(Users user);
    @Query("SELECT u FROM UpperAttributes u WHERE u.users.userIdx = :userIdx AND u.unitList.listIdx = :listIdx")
    Optional<UpperAttributes> findByUserIdxAndListIdx(@Param("userIdx") Long userIdx, @Param("listIdx") Long listIdx);
}
