package com.back.wdam.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="User")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    private String phone;

    private String userState;

    private LocalDateTime userCreated;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(String username, String password, Authority authority, String email, String phone, String userState,LocalDateTime userCreated){
        this.username=username;
        this.password=password;
        this.authority=authority;
        //
        this.email=email;
        this.phone=phone;
        this.userState=userState;
        this.userCreated=userCreated;
    }
}
