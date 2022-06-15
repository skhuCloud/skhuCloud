package com.skhu.cloud.controller;

import com.skhu.cloud.constant.Const;
import com.skhu.cloud.dto.FileDto;
import com.skhu.cloud.dto.version.FileVersionDto;
import com.skhu.cloud.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("") // 기본 디렉토리로
public class MainController {

    private final MainService mainService;

    // 맨 처음에만 불러져야 하는 action method
    @GetMapping("")
    public ModelAndView getMainPage() throws IOException { // directories 로 redirect
        ModelAndView mvc = new ModelAndView("redirect:directories");
        String path = "/Users";
        mvc.addObject("path", path);

        return mvc;
    }

    @GetMapping("directories")
    public ModelAndView clickDirectory(String path, Long pageNumber, String sortBy, String direction, Long jump) throws IOException{ // 페이지네이션 적용된 이동
        ModelAndView mvc = new ModelAndView("main");
        System.out.println("jump : " + jump);
        if (pageNumber == null) {
            pageNumber = Const.INIT_PAGE_NUMBER;
        }

        if (sortBy == null || direction == null) {
            sortBy = "";
            direction = ""; // Blank 로 채워준다.
        }

        List<FileDto> fileDtoList = mainService.createFileDtoList(path);
        Long totalSize = (long) Math.ceil((double) fileDtoList.size() / Const.PAGE_SIZE);

        if (jump != null) { // 0 이 아닐때에만 진행하도록, 점프할때 점프한 페이지의 첫 페이지로 이동할 수 있도록
            Long tempPageNumber = pageNumber;

            tempPageNumber = ((tempPageNumber - 1 + jump) / Const.PAGE_SIZE) * Const.PAGE_SIZE + 1; // 현재 그렇게 만들어놓은 상태이다.

            if (tempPageNumber < 0) {
                pageNumber = 1L;
            } else if (tempPageNumber <= totalSize) { // jump 한 page Number 가 음수가 된 경우는 무조건 1 페이지로 이동해야 한다.
                pageNumber = tempPageNumber;
            }
        }
        System.out.println("pageNumber : " + pageNumber);


        Long start = (pageNumber - 1) / Const.PAGE_SIZE * Const.PAGE_SIZE + 1;
        Long end = (pageNumber - 1) / Const.PAGE_SIZE * Const.PAGE_SIZE + Const.PAGE_SIZE;

        if (end > totalSize) {
            end = totalSize;
        }

        mvc.addObject("nowPath", path);
        mvc.addObject("nowPage", pageNumber);
        mvc.addObject("number", totalSize);
        mvc.addObject("startNumber", start);
        mvc.addObject("endNumber", end);

        mainService.mvcAddObject(mvc , mainService.getDirectoryList(path), mainService.pagingFileDtoList(fileDtoList, pageNumber - 1, sortBy, direction)); // Paging 처리 부분

        return mvc;
    }

    @GetMapping("files")
    public ModelAndView clickFile(String path , Long index) throws IOException{
        // 원래 content 를 직접적으로 넘겼었지만 , versionList 에 담겨있는 content 를 넘기는 식으로 request header to large 문제를 해결 하였음
        ModelAndView mvc = new ModelAndView("filecontent");

        if (index == null) {
            index = 0L;
        }

        // index 값은 , 해당 버전이 몇번째인지이고 , 현재로서는 index = 1이 첫번째 버전이니까 index 가 null 이라면 index = 1 로 설정해주자.
        List<FileVersionDto> versionList = mainService.getVersionList(path);

        mvc.addObject("diff", false);
        mainService.filesMvcAddObject(mvc , FileDto.getExtension(path) , versionList , mainService.getTimeList(versionList),
                mainService.getCodeList(versionList) , path , index , mainService.getComponentName(path));

        return mvc;
    }

    @GetMapping("all")
    public ModelAndView searchAllFile(String path, String key) throws IOException {
        ModelAndView mvc = new ModelAndView("search");

        mvc.addObject("nowPath", path);
        mvc.addObject("fileList", mainService.findSubFile(path, key));

        return mvc;
    }


    // version 간의 차이를 보여주는 action method
    @GetMapping("version")
    public ModelAndView compareVersion(String path , Long index) throws IOException{
        ModelAndView mvc = new ModelAndView("diffTest");
        if(index == null) index = 0L;

        // index 값은 , 해당 버전이 몇번째인지이고 , 현재로서는 index = 1이 첫번째 버전이니까 index 가 null 이라면 index = 1 로 설정해주자.
        List<FileVersionDto> versionList = mainService.getVersionList(path);

        mainService.versionMvcAddObject(mvc , FileDto.getExtension(path) , versionList , mainService.getTimeList(versionList),
                mainService.getCodeList(versionList) , path , index , mainService.getComponentName(path));

        return mvc;
    }
}
