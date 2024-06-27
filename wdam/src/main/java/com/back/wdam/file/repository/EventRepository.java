package com.back.wdam.file.repository;

import com.back.wdam.entity.Event;
import com.back.wdam.entity.UnitBehavior;
import com.back.wdam.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByUsers(Users user);

    @Query("SELECT e FROM Event e WHERE e.users.userIdx = :userIdx AND e.createdAt = :createdAt AND (e.sourceUnit.listIdx = :listIdx OR e.targetUnit.listIdx = :listIdx)")
    Optional<List<Event>> findAllByUserIdxAndListIdx(@Param("userIdx") Long userIdx, @Param("listIdx") Long listIdx, @Param("createdAt") LocalDateTime createdAt);
}
