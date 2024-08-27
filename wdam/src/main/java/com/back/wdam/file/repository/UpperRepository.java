package com.back.wdam.file.repository;

import com.back.wdam.entity.UpperAttributes;
import com.back.wdam.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import java.util.List;

public interface UpperRepository extends JpaRepository<UpperAttributes, Long> {

    List<UpperAttributes> findByUsers(Users user);
    @Query("SELECT u FROM UpperAttributes u WHERE u.users.userIdx = :userIdx AND u.unitList.listIdx = :listIdx AND u.createdAt = :createdAt")
    Optional<List<UpperAttributes>> findAllByUserIdxAndListIdx(@Param("userIdx") Long userIdx, @Param("listIdx") Long listIdx, @Param("createdAt") LocalDateTime createdAt);

    @Query("SELECT u FROM UpperAttributes u WHERE u.unitList.unitId = :id AND u.users.userIdx = :userIdx AND u.createdAt = :createdAt")
    List<UpperAttributes> findAllByUserIdxAndCreatedAt(@Param("id") Long id, @Param("userIdx") Long userIdx, @Param("createdAt") LocalDateTime createdAt);
}
