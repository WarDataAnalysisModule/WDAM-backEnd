package com.back.wdam.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MypageDto {

    private String userName;

    private String phone;

    private String email;

    public MypageDto(String userName, String phone, String email){
        this.userName = userName;
        this.phone = phone;
        this.email = email;
    }
}
