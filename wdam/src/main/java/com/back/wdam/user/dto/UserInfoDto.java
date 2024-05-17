package com.back.wdam.user.dto;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserInfoDto {
    private String userName;
    @Nullable
    private String password;
    private String email;
    private String phone;
}
