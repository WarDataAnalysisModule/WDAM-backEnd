package com.back.wdam.file.repository;

import com.back.wdam.entity.UnitInit;
import com.back.wdam.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import java.util.List;

public interface InitRepository extends JpaRepository<UnitInit, Long> {
    List<UnitInit> findByUsers(Users user);

    @Query("SELECT i FROM UnitInit i WHERE i.users.userIdx = :userIdx AND i.unitList.listIdx = :listIdx")
    Optional<List<UnitInit>> findAllByUserIdxAndListIdx(@Param("userIdx") Long userIdx, @Param("listIdx") Long listIdx);
}
