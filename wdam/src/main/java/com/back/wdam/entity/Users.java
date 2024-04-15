package com.back.wdam.entity;

import com.back.wdam.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(unique = true)
    private String userName;

    private String password;

    @Column(unique = true)
    private String email;

    private String phone;

    private String userState;

    private LocalDateTime userCreated;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Users(String userName, String password, Role role, String email, String phone, String userState, LocalDateTime userCreated){
        this.userName=userName;
        this.password=password;
        this.role = role;
        //
        this.email=email;
        this.phone=phone;
        this.userState=userState;
        this.userCreated=userCreated;
    }
}
