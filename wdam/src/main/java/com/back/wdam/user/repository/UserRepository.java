package com.back.wdam.user.repository;

import com.back.wdam.entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {


    Optional<Users> findByUserIdx(Long userIdx);

    @Modifying
    @Transactional
    @Query("update Users u set u.userName = :userName, u.password = :password, u.phone = :phone, u.email = :email where u.userIdx = :userIdx")
    void updateByUserIdx(@Param("userName") String userId, @Param("password") String password, @Param("phone") String phone, @Param("email") String email, @Param("userIdx") Long userIdx);

    //userName을 기준으로 권한 정보를 가져옴
    Optional<Users> findByUserName(String userName);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);
}
