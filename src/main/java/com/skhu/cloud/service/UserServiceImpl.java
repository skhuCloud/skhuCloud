package com.skhu.cloud.service;

import com.skhu.cloud.config.security.JwtTokenProvider;
import com.skhu.cloud.entity.User;
import com.skhu.cloud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long signup(@RequestBody Map<String, String> user) {
        return userRepository.save(User.builder()
                .username(user.get("username"))
                .password(passwordEncoder.encode(user.get("password")))
                .roles(Collections.singletonList("ROLE_USER")) //최초로 회원 가입기 USER로 설정
                .build()).getId();
    }

    @Override
    @Transactional
    public String login(@RequestBody Map<String, String> user) {
        User member = userRepository.findByUsername(user.get("username"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 ID 입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }
}
