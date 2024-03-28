package com.back.wdam.module.repository;

import com.back.wdam.entity.UnitAttributes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<UnitAttributes, Long> {
}
