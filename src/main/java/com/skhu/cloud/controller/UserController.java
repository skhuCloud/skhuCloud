package com.skhu.cloud.controller;

import com.skhu.cloud.entity.User;
import com.skhu.cloud.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.event.TableColumnModelListener;

//import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController { // 현재는 사용안되고 있는 Controller

    private final UserService userService;

    @GetMapping("signup")
    public ModelAndView signup(){
        return new ModelAndView("signup");
    }

    @PostMapping("signup")
    public ModelAndView signup(User user , String password2){
        ModelAndView mvc = new ModelAndView("login");
        try {
            userService.signup(user , password2);
        } catch (Exception e) {
            log.error("exception : {}" , e.getMessage());
            mvc.setViewName("signup");
            return mvc;
        }
        return new ModelAndView("login");
    }

    @GetMapping("login")
    public ModelAndView login(){
        return new ModelAndView("login");
    }

    @PostMapping("login")
    public ModelAndView login(User user){
        ModelAndView mvc = new ModelAndView("main");
        User result;

        try {
            result = userService.login(user);
        } catch (Exception e) { // Exception 처리
            log.error("exception : {}" , e.getMessage());
            mvc.setViewName("login");
            return mvc;
        }

        mvc.addObject(result);
        return mvc;
    }
}
