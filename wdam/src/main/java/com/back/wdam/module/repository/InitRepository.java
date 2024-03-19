package com.back.wdam.module.repository;

import com.back.wdam.entity.UnitInit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InitRepository extends JpaRepository<UnitInit, Long> {
    Optional<UnitInit> findByUnitId(Integer unitId);
}
