package com.back.wdam.user.dto;

import com.back.wdam.user.entity.Authority;
import com.back.wdam.user.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    private String username;
    private String password;
    private String email;
    private String phone;
    private String userState;
    private LocalDateTime userCreated;

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .authority(Authority.ROLE_USER)
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