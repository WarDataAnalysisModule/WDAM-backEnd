package com.back.wdam.module.repository;

import com.back.wdam.entity.UpperAttributes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpperRepository extends JpaRepository<UpperAttributes, Long> {

}
