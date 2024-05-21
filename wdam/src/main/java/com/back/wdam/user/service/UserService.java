package com.back.wdam.user.service;

import com.back.wdam.entity.Users;
import com.back.wdam.user.dto.MypageDto;
import com.back.wdam.user.dto.UserInfoDto;
import com.back.wdam.user.repository.UserRepository;
import com.back.wdam.util.exception.CustomException;
import com.back.wdam.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public MypageDto getMypage(UserDetails userDetails){

        Optional<Users> user = userRepository.findByUserName(userDetails.getUsername());
        if(user.isEmpty()) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        Long userIdx = user.get().getUserIdx();

        return userRepository.getUserInfo(userIdx);
    }

    public void modifyUser(UserDetails userDetails, UserInfoDto userInfoDto){
        Optional<Users> user = userRepository.findByUserName(userDetails.getUsername());
        if(user.isEmpty()) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        Long userIdx = user.get().getUserIdx();

        if(userInfoDto.getPassword()==null) userRepository.updateByUserIdx(userInfoDto.getPhone(), userInfoDto.getEmail(), userIdx);
        else userRepository.updateByUserIdx(passwordEncoder.encode(userInfoDto.getPassword()),
                     userInfoDto.getPhone(), userInfoDto.getEmail(), userIdx);
    }
}
