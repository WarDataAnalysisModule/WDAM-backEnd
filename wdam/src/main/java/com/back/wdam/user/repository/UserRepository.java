package com.back.wdam.user.repository;

import com.back.wdam.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUserIdx(Long userIdx);

    @Modifying
    @Transactional
    @Query("update User u set u.userId = :userId, u.password = :password, u.phone = :phone, u.email = :email where u.userIdx = :userIdx")
    void updateByUserIdx(@Param("userId") String userId, @Param("password") String password, @Param("phone") String phone, @Param("email") String email, @Param("userIdx") Long userIdx);
}
