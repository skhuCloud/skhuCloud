package com.skhu.cloud.controller;

import com.skhu.cloud.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/") // 기본 디렉토리로
public class MainController {

    private final MainService mainService;

    // 맨 처음에만 불러져야 하는 action method
    @GetMapping("main")
    public ModelAndView getMainPage() throws IOException{
        ModelAndView mvc = new ModelAndView("main");
        String path = "/users/jaeyeonkim/testDirectory";

        mainService.mvcAddObject(mvc , mainService.getDirectoryList(path) , mainService.createFileDtoList(path));

        return mvc;
    }

    @GetMapping("directories")
    public ModelAndView clickDirectory(String path) throws IOException{
        ModelAndView mvc = new ModelAndView("main");

        mainService.mvcAddObject(mvc , mainService.getDirectoryList(path) , mainService.createFileDtoList(path));

        return mvc;
    }

    // 새로운 창을 띄워서 거기다가 file 의 content 를 띄움
    @GetMapping("files")
    public ModelAndView clickFile(String path) throws IOException{
        ModelAndView mvc = new ModelAndView("filecontent");

        List<String> time = new ArrayList<>();
        List<Integer> code = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            time.add(i + 1 + "시");
            code.add(i + 1);
        } // 여기서는 잘 넘기고 있음 , 근데 arr를 지금 reference 값을 그대로 주소값을 String 으로 변경해서 받아오고 있어서 지금 안되고 있는 것임

        mvc.addObject("time" , time);
        mvc.addObject("code" , code);
        mvc.addObject("content" , mainService.readFile(path));

        return mvc;
    }
}
