package com.skhu.cloud.controller;

import com.skhu.cloud.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/") // 기본 디렉토리로
public class MainController {

    private final MainService mainService;

    // 맨 처음에만 불러져야 하는 action method
    @GetMapping("main")
    public ModelAndView getMainPage(HttpSession session) {
        ModelAndView mvc = new ModelAndView("main");
        String path = "/users/jaeyeonkim/testDirectory";

        List<String> directoryList = mainService.getDirectoryList(path);

        // 디렉토리 리스트와 , path를 session 으로 관리한다.
        session.setAttribute("path", path);
        session.setAttribute("directory" , directoryList);

        // 이렇게 path 리스트와 files 리스트를 순서대로 넘겨보자.
        mainService.mvcAddObject(mvc , directoryList , mainService.createFileDtoList(path));

        return mvc;
    }

    @GetMapping("main/{index}")
    public ModelAndView clickFile(@PathVariable Long index , HttpSession session){
        ModelAndView mvc = new ModelAndView("main");

        String path = (String)session.getAttribute("path");

        path = mainService.returnPath(path , index);

        mainService.mvcAddObject(mvc , mainService.getDirectoryList(path) , mainService.createFileDtoList(path));
        session.setAttribute("path", path); // path 갱신
        session.setAttribute("directory" , mainService.getDirectoryList(path));

        return mvc;
    }

    @GetMapping("main/{index}/{jump}")
    public ModelAndView clickFolder(@PathVariable Long index , @PathVariable String jump , HttpSession session){
        ModelAndView mvc = new ModelAndView("main");

        String path = mainService.moveDirectory(index , (ArrayList)session.getAttribute("directory"));

        session.setAttribute("path" , path);
        session.setAttribute("directory" , mainService.getDirectoryList(path));

        mainService.mvcAddObject(mvc , mainService.getDirectoryList(path) , mainService.createFileDtoList(path));

        return mvc;
    }
}
