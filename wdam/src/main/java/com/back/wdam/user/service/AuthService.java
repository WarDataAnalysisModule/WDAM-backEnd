package com.back.wdam.user.service;

import com.back.wdam.entity.RefreshToken;
import com.back.wdam.entity.Users;
import com.back.wdam.user.dto.LoginDto;
import com.back.wdam.user.dto.TokenDto;
import com.back.wdam.user.dto.TokenRequestDto;
import com.back.wdam.user.dto.UserRequestDto;
import com.back.wdam.user.jwt.TokenProvider;
import com.back.wdam.user.repository.RefreshTokenRepository;
import com.back.wdam.user.repository.UserRepository;
import com.back.wdam.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public ApiResponse signup(UserRequestDto userRequestDto) {
        // username, email check
        if (userRepository.existsByUserName(userRequestDto.getUsername())||userRepository.existsByEmail(userRequestDto.getEmail()) ) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Users user = userRequestDto.toUser(passwordEncoder);
        userRepository.save(user);
        ApiResponse response=new ApiResponse("1000",null);
        return response;
    }

    // 이미 가입된 회원인 경우 Exeption Handler
    @Transactional
    public ApiResponse signupOrHandleError(UserRequestDto userRequestDto) {
        try {
            return signup(userRequestDto);
        } catch (RuntimeException e) {
            return new ApiResponse("600", null);
        }
    }
    @Transactional
    public TokenDto login(LoginDto loginDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword());

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    //access token 만료 시 재발급 받기
    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 유저 정보 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. DB에서 유저 정보를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

    @Transactional
    public void logout(TokenRequestDto logout){
        //1. Access Tocken 검증
        if(!tokenProvider.validateToken(logout.getAccessToken())){
            throw new RuntimeException("Access Token 이 유효하지 않습니다.");
        }

        //2. Access Token에서 userName(id)을 가져옴
        Authentication authentication = tokenProvider.getAuthentication(logout.getAccessToken());

        //3. DB에서 해당 refresh token 삭제
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("이미 로그아웃 된 사용자입니다."));

        refreshTokenRepository.delete(refreshToken);

    }
}