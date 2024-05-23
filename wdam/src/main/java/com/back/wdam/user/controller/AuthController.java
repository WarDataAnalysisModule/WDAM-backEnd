package com.back.wdam.user.controller;

import com.back.wdam.user.dto.LoginDto;
import com.back.wdam.user.dto.TokenDto;
import com.back.wdam.user.dto.TokenRequestDto;
import com.back.wdam.user.dto.UserRequestDto;
import com.back.wdam.user.jwt.JwtFilter;
import com.back.wdam.user.service.AuthService;
import com.back.wdam.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(authService.signupOrHandleError(userRequestDto));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginDto loginDto) {

        // 토큰 발급
        TokenDto token=authService.login(loginDto);

        //response 생성
        ApiResponse apiResponse=new ApiResponse("1000", null);

        //http header에 access token 저장
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER,"Bearer "+token.getAccessToken());
        //http header에 refresh token 저장
        httpHeaders.set("refreshToken",token.getRefreshToken());
        //cookie에 refresh token 저장
        ResponseCookie responseCookie=ResponseCookie.from("refreshToken", token.getRefreshToken())
                .httpOnly(true) // 쿠키 탈취 방지
                .secure(true)   // https 요청으로만 쿠키 주고받기 가능
                .domain("localhost")    // 특정 도메인에서만 사용, aws 시 수정
                //.domain("ec2-3-36-242-36.ap-northeast-2.compute.amazonaws.com")
                .maxAge(Duration.ofDays(7)) //쿠키 만료기간 7일
                .sameSite("None") // 크로스 사이트 요청에서 쿠키를 허용
                .build();
        apiResponse.setData(token);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .headers(httpHeaders)
                .body(apiResponse);
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        ApiResponse apiResponse=new ApiResponse("1000",null);
        TokenDto token=authService.reissue(tokenRequestDto);
        apiResponse.setData(token);
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER,"Bearer "+token.getAccessToken());
        //http header에 refresh token 저장
        httpHeaders.set("refreshToken",token.getRefreshToken());
        ResponseCookie responseCookie=ResponseCookie.from("refreshToken", token.getRefreshToken())
                .httpOnly(true) // 쿠키 탈취 방지
                .secure(true)   // https 요청으로만 쿠키 주고받기 가능
                .domain("localhost")    // 특정 도메인에서만 사용, aws 시 수정
                //.domain("ec2-3-36-242-36.ap-northeast-2.compute.amazonaws.com")
                .maxAge(Duration.ofDays(7)) //쿠키 만료기간 7일
                .sameSite("None") // 크로스 사이트 요청에서 쿠키를 허용
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .headers(httpHeaders)
                .body(apiResponse);
    }
}
