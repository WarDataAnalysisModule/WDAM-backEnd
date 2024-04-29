package com.back.wdam.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

// 로그인 시 사용하는 정보
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotNull
    private String userName;

    @NotNull
    private String password;
}