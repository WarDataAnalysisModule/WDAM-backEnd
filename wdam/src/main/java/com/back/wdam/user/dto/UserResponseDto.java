package com.back.wdam.user.dto;

import com.back.wdam.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String username;

    public static UserResponseDto of(Users user) {
        return new UserResponseDto(user.getUserName());
    }
}
