package com.back.wdam.module.repository;

import com.back.wdam.entity.UnitList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitListRepository extends JpaRepository<UnitList, Long> {
    Optional<UnitList> findByUnitId(Long unitList);
}
