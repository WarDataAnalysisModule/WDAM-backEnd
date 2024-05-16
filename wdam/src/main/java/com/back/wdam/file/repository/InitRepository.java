package com.back.wdam.file.repository;

import com.back.wdam.entity.UnitInit;
import com.back.wdam.entity.UpperAttributes;
import com.back.wdam.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InitRepository extends JpaRepository<UnitInit, Long> {
    List<UnitInit> findByUsers(Users user);
}
