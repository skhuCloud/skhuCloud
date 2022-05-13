package com.skhu.cloud.service.Impl;

import com.skhu.cloud.entity.User;
import com.skhu.cloud.repository.UserRepository;
import com.skhu.cloud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import java.util.Collections;
//import java.util.Map;
import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long signup(User user , String password2){
        if(!user.getPassword().equals(password2)) throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        return userRepository.save(User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .name(user.getName())
                .build()).getId();
    }

    @Override
    @Transactional
    public User login(User user){
        User result = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> {throw new EntityNotFoundException();});
        if(!user.getPassword().equals(result.getPassword())){
             throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
         }

        return result;
    }
}
