package com.skhu.cloud.controller;

import com.skhu.cloud.entity.User;
import com.skhu.cloud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

//import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * user/signup 으로 signup page 호출
     */
    @GetMapping("signup")
    public ModelAndView signup(){
        return new ModelAndView("signup");
    }

    /**
     * 회원가입
     * signup 에서 입력을 다 하고 회원 가입 버튼을 누르면
     * PostMapping user/signup 을 요청해서 , 해당 action method 를 호출한다.
     * 그 다음에 login page 로 이동 시킨다.
     */
//    @PostMapping("signup")
//    public ModelAndView signup(@RequestBody Map<String, String> user , User userDto) {
//        userService.signup(user);
//        return new ModelAndView("login");
//    }

    @PostMapping("signup")
    public ModelAndView signup(User user , String password2){
        userService.signup(user , password2);
        return new ModelAndView("login");
    }

    /**
     * user/login 으로 login page 호출
     */
    @GetMapping("login")
    public ModelAndView login(){
        return new ModelAndView("login");
    }

    /**
     * 이제 login page 에서 user/login url을 요청하면
     * token 을 요청하게 되는데 , 받은 token 의 값이 올바르지 않다면
     * 즉 null 값이 넘어오게 된다면 , 다시 login page 로 (혹은 exception 이 넘어오면)
     * 그게 아니라면 main page 로 넘어가게끔 하는데 , local storage 에다가 저장하던가 해야한다(토큰을)
     */
//    @PostMapping(
//            path = "login" ,
//            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
//    )
//    public ModelAndView login(@RequestBody Map<String, String> user) {
//        ModelAndView mvc = new ModelAndView("main");
//        String token = userService.login(user);
//
//        if(token == null || token.isBlank()) {
//            mvc.setViewName("login");
//            return mvc;
//        }
//
//        mvc.addObject("jwt" , token);
//        return mvc;
//    }

    /**
     * username , password 비교 후에 받은 값을 통해서,
     * 로그인 성공했는지 안했는지를 확인한다.
     */
    @PostMapping("login")
    public ModelAndView login(User user){
        ModelAndView mvc = new ModelAndView("main");

        User result = userService.login(user);
        System.out.println(result);

        if(result == null){
            mvc.setViewName("login");
            return mvc;
        }

        mvc.addObject(result);
        return mvc;
    }
}
