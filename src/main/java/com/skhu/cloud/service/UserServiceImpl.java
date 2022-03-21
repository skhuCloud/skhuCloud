package com.skhu.cloud.service;

//import com.skhu.cloud.config.security.JwtTokenProvider;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import com.skhu.cloud.entity.User;
import com.skhu.cloud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import java.util.Collections;
//import java.util.Map;
import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

//    private final PasswordEncoder passwordEncoder;
//    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

//    @Override
//    @Transactional
//    public Long signup(@RequestBody Map<String, String> user) {
//        return userRepository.save(User.builder()
//                .username(user.get("username"))
//                .password(passwordEncoder.encode(user.get("password")))
//                .email(user.get("email"))
//                .name(user.get("name"))
//                .roles(Collections.singletonList("ROLE_USER")) //최초로 회원 가입기 USER로 설정
//                .build()).getId();
//    }

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

    /**
     * 유저의 정보가 DB에 저장되어 있는 값과 동일하다면 , createToken 을 통해서 토큰 발급
     */
//    @Override
//    @Transactional
//    public String login(@RequestBody Map<String, String> user) {
//        User member = userRepository.findByUsername(user.get("username"))
//                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 ID 입니다."));
//        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
//            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
//        }
//        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
//    }

    /**
     * 여기서 password 와 , username 확인해서 , 아니면 exception throw
     * 맞으면 반환
     */
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
