package com.skhu.cloud.controller;

import com.skhu.cloud.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/") // 기본 디렉토리로
public class MainController {

    private final MainService mainService;

    // 맨 처음에만 불러져야 하는 action method
    @GetMapping("main")
    public ModelAndView getMainPage() {
        ModelAndView mvc = new ModelAndView("main");
        String path = "/users/jaeyeonkim/testDirectory";

        System.out.println(mainService.getDirectoryList(path));

        // 이렇게 path 리스트와 files 리스트를 순서대로 넘겨보자.
        mainService.mvcAddObject(mvc , mainService.getDirectoryList(path) , mainService.createFileDtoList(path));

        return mvc;
    }

    @GetMapping("directories")
    public ModelAndView clickDirectory(String path){
        // 결국 그렇게 path 를 받았음
        ModelAndView mvc = new ModelAndView("main");

        mainService.mvcAddObject(mvc , mainService.getDirectoryList(path) , mainService.createFileDtoList(path));

        return mvc;
    }

    // 새로운 창을 띄워서 거기다가 file 의 content 를 띄움
    @GetMapping("files")
    public ModelAndView clickFile(String path) throws IOException {
        ModelAndView mvc = new ModelAndView("filecontent");

        mvc.addObject("content" , mainService.readFile(path));

        System.out.println(mainService.readFile(path));

        return mvc;
    }
}
