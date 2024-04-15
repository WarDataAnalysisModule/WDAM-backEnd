package com.back.wdam.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MypageDto {

    private String userId;

    private String password;

    private String phone;

    private String email;

    public MypageDto(String userId, String password, String phone, String email){
        this.userId = userId;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }
}
