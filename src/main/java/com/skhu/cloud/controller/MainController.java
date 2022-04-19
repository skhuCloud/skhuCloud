package com.skhu.cloud.controller;

import com.skhu.cloud.dto.FileDto;
import com.skhu.cloud.dto.FileVersionDto;
import com.skhu.cloud.entity.FolderVersion;
import com.skhu.cloud.repository.FolderVersionRepository;
import com.skhu.cloud.service.FolderService;
import com.skhu.cloud.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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

        mvc.addObject("nowPath" , path);
        mainService.mvcAddObject(mvc , mainService.getDirectoryList(path) , mainService.createFileDtoList(path));

        return mvc;
    }

    @GetMapping("directories")
    public ModelAndView clickDirectory(String path) throws IOException{
        ModelAndView mvc = new ModelAndView("main");

        mvc.addObject("nowPath" , path);
        mainService.mvcAddObject(mvc , mainService.getDirectoryList(path) , mainService.createFileDtoList(path));

        return mvc;
    }

    @GetMapping("files")
    public ModelAndView clickFile(String path , Long index) throws IOException{
        // 원래 content 를 직접적으로 넘겼었지만 , versionList 에 담겨있는 content 를 넘기는 식으로 request header to large 문제를 해결 하였음
        ModelAndView mvc = new ModelAndView("filecontent");
        if(index == null) index = 0L;

        // index 값은 , 해당 버전이 몇번째인지이고 , 현재로서는 index = 1이 첫번째 버전이니까 index 가 null 이라면 index = 1 로 설정해주자.
        List<FileVersionDto> versionList = mainService.getVersionList(path);

        mainService.filesMvcAddObject(mvc , FileDto.getExtension(path) , versionList , mainService.getTimeList(versionList),
                mainService.getCodeList(versionList) , path , index , mainService.getComponentName(path));

        return mvc;
    }

    // version 간의 차이를 보여주는 action method
    @GetMapping("version")
    public ModelAndView compareVersion(String path , Long index) throws IOException{
        /*
        현재 path 는 그냥 중요하고 , 그리고 현재의 index 도 중요하다
        그래서 이전처럼 filesMvcAddObject 에서 diff content 도 추가적으로 보내주어야 한다.
         */

        ModelAndView mvc = new ModelAndView("diffTest");
        if(index == null) index = 0L;

        // index 값은 , 해당 버전이 몇번째인지이고 , 현재로서는 index = 1이 첫번째 버전이니까 index 가 null 이라면 index = 1 로 설정해주자.
        List<FileVersionDto> versionList = mainService.getVersionList(path);

        mainService.versionMvcAddObject(mvc , FileDto.getExtension(path) , versionList , mainService.getTimeList(versionList),
                mainService.getCodeList(versionList) , path , index , mainService.getComponentName(path));

        return mvc;
    }

    private final FolderVersionRepository folderVersionRepository;
    private final FolderService folderService;

    // 이런식으로 구성하면 folder version 도 구현 그냥 가능할 듯하다.
    // 즉 어떠한 version 을 선택하면 , 해당 버전의 밑에 있는 버전들이 불러져 올 것이다.
    @GetMapping("folder/version")
    public ModelAndView folderVersion(Long parent,
                                      @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                                              LocalDateTime localDateTime){

        ModelAndView mvc = new ModelAndView("folderversiontest");


        if(parent == null) parent = 0L; // path 가 비어있는 경우에는 root 로
        if(localDateTime == null) localDateTime = LocalDateTime.now();

        System.out.println(parent);

        mvc.addObject("folderVersionList" , folderService.getFolderVersionDtoList(folderService.getFolderVersionList(parent , localDateTime)));
        mvc.addObject("nowPath" , parent);
        mvc.addObject("directoryList" , folderService.getDirectoryList(parent , localDateTime));

        return mvc;
    }

    @PostMapping("")
    public void save(Long parent , String status , String content , LocalDateTime time ,
                     String kind , String title , Long size , Long componentId){
        folderVersionRepository.save(FolderVersion.builder()
                .parent(parent)
                .status(status)
                .content(content)
                .time(time)
                .kind(kind)
                .title(title)
                .size(size)
                .componentId(componentId)
                .build());
    }
}
