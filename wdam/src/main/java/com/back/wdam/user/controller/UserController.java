package com.back.wdam.user.controller;

import com.back.wdam.user.dto.MypageDto;
import com.back.wdam.user.dto.TokenRequestDto;
import com.back.wdam.user.dto.UserInfoDto;
import com.back.wdam.user.service.AuthService;
import com.back.wdam.user.service.UserService;
import com.back.wdam.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    //마이페이지 조회
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<MypageDto>> getMypage(@AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity.ok(new ApiResponse("1000", userService.getMypage(userDetails)));
    }

    //마이페이지 수정
    @PatchMapping("/users/update")
    public ResponseEntity<ApiResponse<MypageDto>> modifyUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserInfoDto userInfoDto){

        userService.modifyUser(userDetails, userInfoDto);

        return ResponseEntity.ok(new ApiResponse("1000", userService.getMypage(userDetails)));
    }

    //로그아웃
    @PostMapping("/users/logout")
    public ResponseEntity<ApiResponse> logout(@RequestBody TokenRequestDto tokenRequestDto){
        authService.logout(tokenRequestDto);
        return ResponseEntity.ok(new ApiResponse("1000", null));
    }
}
