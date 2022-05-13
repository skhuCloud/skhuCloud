package com.skhu.cloud.service;

//import org.springframework.web.bind.annotation.RequestBody;
import com.skhu.cloud.entity.User;
import org.springframework.stereotype.Service;

//import java.util.Map;

@Service
public interface UserService {
    Long signup(User user , String password2);

    User login(User user);
}
