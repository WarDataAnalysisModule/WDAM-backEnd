package com.back.wdam.user.controller;

import com.back.wdam.user.dto.LoginDto;
import com.back.wdam.user.dto.TokenDto;
import com.back.wdam.user.dto.UserRequestDto;
import com.back.wdam.user.jwt.JwtFilter;
import com.back.wdam.user.service.AuthService;
import com.back.wdam.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

class ResponseData<T>{
    List<T> data=new ArrayList<>();

    public void add(T item){
        data.add(item);
    }
}

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
        ResponseData<String> responseData=new ResponseData<>();
        responseData.add("grant type:");
        responseData.add(token.getGrantType());
        responseData.add("access token:");
        responseData.add(token.getAccessToken());
        responseData.add("refresh token:");
        responseData.add(token.getRefreshToken());
        responseData.add("AccessTokenExpiresIn:");
        responseData.add(String.valueOf(token.getAccessTokenExpiresIn()));
        apiResponse.setData(responseData.data);

        //http header에 access token 저장
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER,"Bearer"+token.getAccessToken());
        //cookie에 refresh token 저장
        ResponseCookie responseCookie=ResponseCookie.from("refreshToken", token.getRefreshToken())
                .httpOnly(true) // 쿠키 탈취 방지
                .secure(true)   // https 요청으로만 쿠키 주고받기 가능
                .domain("localhost")    // 특정 도메인에서만 사용, aws 시 수정
                .maxAge(Duration.ofDays(7)) //쿠키 만료기간 7일
                .build();
        //return new ResponseEntity<>(apiResponse, httpHeaders, HttpStatus.OK);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .headers(httpHeaders)
                .body(apiResponse);
    }

    @GetMapping("/test")
    public ResponseEntity<ApiResponse> testapi(){
        ApiResponse apiResponse=new ApiResponse("1000",null);
        return ResponseEntity.ok(apiResponse);
    }
}