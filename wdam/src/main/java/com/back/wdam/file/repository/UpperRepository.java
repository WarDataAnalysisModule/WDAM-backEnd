package com.back.wdam.file.repository;

import com.back.wdam.entity.UpperAttributes;
import com.back.wdam.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UpperRepository extends JpaRepository<UpperAttributes, Long> {

    List<UpperAttributes> findByUsers(Users user);
}
