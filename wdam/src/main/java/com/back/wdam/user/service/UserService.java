package com.back.wdam.user.service;

import com.back.wdam.entity.User;
import com.back.wdam.user.dto.MypageDto;
import com.back.wdam.user.repository.UserRepository;
import com.back.wdam.util.exception.CustomException;
import com.back.wdam.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public MypageDto getMypage(Long userIdx){

        Optional<User> user = userRepository.findByUserIdx(userIdx);

        if(user.isEmpty()){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        else{
            return new MypageDto(user.get().getUserId(), user.get().getPassword(), user.get().getPhone(),
                    user.get().getEmail());
        }
    }

    public void modifyUser(Long userIdx, MypageDto mypageDto){
        Optional<User> user = userRepository.findByUserIdx(userIdx);

        if(user.isEmpty()){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        else{
            userRepository.updateByUserIdx(mypageDto.getUserId(), mypageDto.getPassword(),
                     mypageDto.getPhone(), mypageDto.getEmail(), userIdx);
        }
    }
}
