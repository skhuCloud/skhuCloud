package com.skhu.cloud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    @GetMapping("classList")
    public ModelAndView getSearch() {
        return new ModelAndView("tables");
    }
}
