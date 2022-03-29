package com.skhu.cloud.controller;

import com.skhu.cloud.dto.FileDto;
import com.skhu.cloud.dto.VersionDto;
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
        String path = "/Users";

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

        List<VersionDto> versionList = mainService.getVersionList(path);

        List<String> time = mainService.getTimeList(versionList);
        List<Long> code = mainService.getCodeList(versionList); // method 로 구현

        // versionList , time , code , content 등의 object 들을 mvc 에다가 등록
        mvc.addObject("extension" , FileDto.getExtension(path));
        mvc.addObject("versionList" , versionList);
        mvc.addObject("time" , time);
        mvc.addObject("code" , code);
        mvc.addObject("content" , mainService.readFile(path));


        return mvc;
    }



}
