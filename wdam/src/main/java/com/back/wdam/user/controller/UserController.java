package com.back.wdam.user.controller;

import com.back.wdam.user.dto.MypageDto;
import com.back.wdam.user.service.UserService;
import com.back.wdam.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //마이페이지 조회
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<MypageDto>> getMypage(Long userIdx){
        return ResponseEntity.ok(ApiResponse.success(userService.getMypage(userIdx)));
    }

    //마이페이지 수정
    @PatchMapping("/users/update")
    public ResponseEntity<ApiResponse<MypageDto>> modifyUser(Long userIdx, @RequestBody MypageDto mypageDto){

        userService.modifyUser(1L, mypageDto);

        return ResponseEntity.ok(ApiResponse.success(userService.getMypage(1L)));
    }

    //로그아웃
    //https://velog.io/@minwest/Spring-Security-jwt%EB%A1%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8%EB%A1%9C%EA%B7%B8%EC%95%84%EC%9B%83-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0
}
