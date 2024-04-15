package com.back.wdam.user.dto;

import com.back.wdam.entity.Users;
import com.back.wdam.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    private String username;
    private String password;
    private String email;
    private String phone;
    private String userState;
    private LocalDateTime userCreated;

    public Users toUser(PasswordEncoder passwordEncoder) {
        return Users.builder()
                .userName(username)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .email(email)
                .phone(phone)
                .userState("active")
                .userCreated(LocalDateTime.now())
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}