package com.skhu.cloud.service;

//import org.springframework.web.bind.annotation.RequestBody;
import com.skhu.cloud.entity.User;
import org.springframework.stereotype.Service;

//import java.util.Map;

@Service
public interface UserService {
    // 회원가입 메소드
    Long signup(User user , String password2);

    // 로그인 메소드
    User login(User user);
}
