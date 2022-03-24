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

    // 파일이나 폴더를 클릭할 때 , 불러지는 action method
    // 근데 파일이면 창이 하나가 더 뜨면서 읽을 수 있어야 하고 , 폴더면 다음 디렉토리로 이동해야 한다.
    @GetMapping("main/{index}")
    public ModelAndView clickFile(@PathVariable Long index , HttpSession session){
        // 현재 path 가 어딘지 파악하고 , 해당 list의 index를 반환받으면 될 것 같다.
        // 근데 현재 path가 어떤지 어떻게 계속 유지하지?
        ModelAndView mvc = new ModelAndView("main");

        // 사실 여기서 해당 path 가
        String path = (String)session.getAttribute("path");
//        String clickPath = mainService.returnPath(path , index); // 이게 근데 파일인지 확인이 필요함
        path = mainService.returnPath(path , index);

        mainService.mvcAddObject(mvc , mainService.getDirectoryList(path) , mainService.createFileDtoList(path));
        session.setAttribute("path", path); // path 갱신
        session.setAttribute("directory" , mainService.getDirectoryList(path));

        return mvc;
    }

    // 이전 디렉토리로 이동하려고 할 때 불러지는 action method
    // 이게 생각이 안난다 , 어떻게 하면 디렉토리를 누른건지 알 수 있을까?
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
