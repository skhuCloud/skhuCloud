package com.skhu.cloud.controller;

import com.skhu.cloud.constant.Const;
import com.skhu.cloud.dto.FileDto;
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
@RequestMapping("") // 기본 디렉토리로
public class MainController {

    private final MainService mainService;

    @GetMapping("")
    public ModelAndView getMainPage() throws IOException { // 사이트 첫 방문 api
        ModelAndView mvc = new ModelAndView("redirect:directories");
        String path = mainService.getRootPath();

        mvc.addObject("path", path);

        return mvc;
    }

    @GetMapping("directories")
    public ModelAndView clickDirectory(String path, Long pageNumber, String sortBy, String direction, Long jump) throws IOException { // 디렉토리 이동 api
        ModelAndView mvc = new ModelAndView("main");

        if (pageNumber == null) {
            pageNumber = Const.INIT_PAGE_NUMBER;
        }

        if (sortBy == null || direction == null) {
            sortBy = "";
            direction = ""; // Blank 로 채워준다.
        }

        List<FileDto> fileDtoList = mainService.createFileDtoList(path, sortBy, direction);
        Long[] pagingInfo = mainService.calculatePageNumber(fileDtoList, jump, pageNumber); // pagination 정보를 반환

        mainService.directoriesAddObject(mvc , mainService.getDirectoryList(path), mainService.pagingFileDtoList(fileDtoList, (pagingInfo[0] - 1)),
                path, pagingInfo[0], pagingInfo[1], pagingInfo[2], sortBy, direction); // Paging 처리 부분

        return mvc;
    }

    @GetMapping("files")
    public ModelAndView clickFile(String path) throws IOException{ // file 내용 조회 api
        ModelAndView mvc = new ModelAndView("filecontent");
        mainService.filesMvcAddObject(mvc , false, FileDto.getExtension(path) , path , mainService.getComponentName(path));

        return mvc;
    }

    @GetMapping("all")
    public ModelAndView searchAllFile(String path, String key, @RequestParam(required = false) List<FileDto> fileDtoList,
                                      @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) throws IOException { // Search api
        ModelAndView mvc = new ModelAndView("search");
        List<FileDto> addList = null;

        if (fileDtoList != null) { // 정렬 o
            mvc.addObject("sortBy", sortBy);
            mvc.addObject("direction", direction);
            addList = mainService.sortByFileDtoList(fileDtoList, sortBy, direction);
        }

        if (fileDtoList == null) { // 정렬 x
            addList = mainService.findSubFile(path, key.split("\\?"));
        }

        mainService.searchMvcAddObject(mvc, path, addList);

        return mvc;
    }

    @PostMapping("all")
    public ModelAndView postAllFile(String path, String key, @RequestBody(required = false) List<FileDto> fileDtoList,
                                      @RequestBody(required = false) String sortBy, @RequestBody(required = false) String direction) throws IOException { // Search api
        ModelAndView mvc = new ModelAndView("search");
        List<FileDto> addList = null;
        System.out.println("Post ~~~~~~~~~~~~~~~~~~~~ !!!!!!!!!!!!!!!!!");

        if (fileDtoList != null) { // 정렬 o
            mvc.addObject("sortBy", sortBy);
            mvc.addObject("direction", direction);
            addList = mainService.sortByFileDtoList(fileDtoList, sortBy, direction);
        }

        if (fileDtoList == null) { // 정렬 x
            addList = mainService.findSubFile(path, key.split("\\?"));
        }

        mainService.searchMvcAddObject(mvc, path, addList);

        return mvc;
    }
}
