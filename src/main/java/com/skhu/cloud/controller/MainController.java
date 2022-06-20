package com.skhu.cloud.controller;

import com.skhu.cloud.constant.Const;
import com.skhu.cloud.dto.FileDto;
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
        // 원래 content 를 직접적으로 넘겼었지만 , versionList 에 담겨있는 content 를 넘기는 식으로 request header to large 문제를 해결 하였음
        ModelAndView mvc = new ModelAndView("filecontent");
        mainService.filesMvcAddObject(mvc , false, FileDto.getExtension(path) , path , mainService.getComponentName(path));

        return mvc;
    }

    @GetMapping("all")
    public ModelAndView searchAllFile(String path, String key) throws IOException { // Search api
        ModelAndView mvc = new ModelAndView("search");
        mainService.searchMvcAddObject(mvc, path, mainService.findSubFile(path, key.split("\\?")));

        return mvc;
    }
}
