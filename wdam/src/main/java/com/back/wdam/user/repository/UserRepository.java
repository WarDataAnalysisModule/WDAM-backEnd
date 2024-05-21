package com.back.wdam.user.repository;

import com.back.wdam.entity.Users;
import com.back.wdam.user.dto.MypageDto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    @Modifying
    @Transactional
    @Query("update Users u set u.password = :password, u.phone = :phone, u.email = :email where u.userIdx = :userIdx")
    void updateByUserIdx(@Param("password") String password, @Param("phone") String phone, @Param("email") String email, @Param("userIdx") Long userIdx);

    @Modifying
    @Transactional
    @Query("update Users u set u.phone = :phone, u.email = :email where u.userIdx = :userIdx")
    void updateByUserIdx(@Param("phone") String phone, @Param("email") String email, @Param("userIdx") Long userIdx);

    //userName을 기준으로 권한 정보를 가져옴
    Optional<Users> findByUserName(String userName);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    @Query("SELECT NEW com.back.wdam.user.dto.MypageDto(u.userName, u.phone, u.email) from Users u where u.userIdx = ?1")
    //@Query(value = "select u.userName, u.phone, u.email from Users u where u.userIdx = ?1")
    MypageDto getUserInfo(@Param("userIdx") Long userIdx);

}
