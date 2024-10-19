package com.back.wdam.user.dto;

import lombok.*;

// 토큰 정보 response 시 사용
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
}