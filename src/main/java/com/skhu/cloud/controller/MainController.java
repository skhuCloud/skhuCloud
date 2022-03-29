package com.skhu.cloud.controller;

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

    /*
    일단 version 들을 다 던져야한다.
    그 version 을 가지고 , 예를 들어서 version 이 4개라면 , 혹은 10 개 이상이라면?
    해당 version에 일단 , time(LocalDateTime , content) 등의 내용은 담고 있어야 한다.
    그리고 versionId 들이 있다.

    그러면
    3개로 일단은 좁혀진다.
    id , time , content 등으로 좁혀지고,
    더 가면 해당 content 의 코드량 이라든가 , 해당 코드의 컴파일 횟수?
    컴파일 횟수 가져오는 것은 조금 더 생각해봐야 할 듯

    일단 그러면 version 은 마련이 되어있다고 가정하고,
    version 기능을 어떻게 쓸것인가,

    일단 version 을 같이 처음부터 던져줄 것인가
    아니면 나중에 던져줄 것인가
    근데 , version 을 같이 던져주는 것이 나을 것 같다.
    왜냐하면 time , code 도 같이 넘기기 때문이다.
    그래서 versionList 를 하나 만들어서 넘겨도 될 것 같다.

    mvc.addObject("versionList" , versionList); versionList 이렇게 객체를 직접 넘기는 것이 좋을 것이다
    version 은 어떻게 할까?
    만약 dababase랑 연결한다고 가정했을 때 , 해당 path 로 db를 검색한다고 했을 떄 , 해당 path 에 맞는 version 이 쫘르륵 있을 것이다.
    그러면 거기있는 version 다 가져오면 된다.

    그러면 이제 거기있는 거 파일 하나하나 읽어오고,
    그리고 파일 time , content 로 가져오면 된다.

    일단 나중에는 id 로 할 건데 , 지금은 그냥 id 하나하나 넣어서 가져오는 것으로 하자,
    content 는 현재 파일의 content 를 넣어놓자.
    짜피 가공을 해야하기 때문이다 , 위에서 time , code 뽑아낼 때 짜피 가공해야함

    그러니까 readFile 로 content 에 넣어서 , 저장하면 된다.
     */

    // 새로운 창을 띄워서 거기다가 file 의 content 를 띄움
    @GetMapping("files")
    public ModelAndView clickFile(String path) throws IOException{
        ModelAndView mvc = new ModelAndView("filecontent");

        List<VersionDto> versionList = mainService.getVersionList(path);

        List<String> time = mainService.getTimeList(versionList);
        List<Long> code = mainService.getCodeList(versionList); // method 로 구현

        // versionList , time , code , content 등의 object 들을 mvc 에다가 등록
        mvc.addObject("versionList" , versionList);
        mvc.addObject("time" , time);
        mvc.addObject("code" , code);
        mvc.addObject("content" , mainService.readFile(path));

        return mvc;
    }



}
