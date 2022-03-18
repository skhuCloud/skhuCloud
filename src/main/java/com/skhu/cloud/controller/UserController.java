package com.skhu.cloud.controller;

import com.skhu.cloud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // 회원 가입
    @PostMapping("join")
    public @ResponseBody Long signup(@RequestBody Map<String, String> user) {
        return userService.signup(user);
    }

    // 로그인
    @PostMapping("login")
    public @ResponseBody String login(@RequestBody Map<String, String> user) {
        return userService.login(user);
    }
}
