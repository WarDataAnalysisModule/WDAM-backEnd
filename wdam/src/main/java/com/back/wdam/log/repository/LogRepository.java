package com.back.wdam.log.repository;

import com.back.wdam.entity.ResultLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<ResultLog, Long> {

    @Transactional
    ResultLog saveAndFlush(ResultLog resultLog);

    @Query("SELECT r FROM ResultLog r WHERE r.users.userIdx = :userIdx AND r.logCreated = :logCreated ORDER BY r.createdAt DESC")
    List<ResultLog> findByUserIdAndLogCreated(@Param("userIdx") Long userId, @Param("logCreated") LocalDateTime logCreated);
}
