package com.skhu.cloud.service;

//import org.springframework.web.bind.annotation.RequestBody;
import com.skhu.cloud.entity.User;

//import java.util.Map;

public interface UserService {

//    Long signup(@RequestBody Map<String , String> user);
//
//    String login(@RequestBody Map<String , String> user);

    Long signup(User user , String password2);

    User login(User user);
}
