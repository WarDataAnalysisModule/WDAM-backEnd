package com.back.wdam.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserInfoDto {
    private String userName;
    private String password;
    private String email;
    private String phone;
}
