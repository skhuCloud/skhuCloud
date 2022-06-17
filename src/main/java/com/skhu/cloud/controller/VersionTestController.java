package com.skhu.cloud.controller;

import com.skhu.cloud.constant.Const;
import com.skhu.cloud.dto.diff.FolderDiffDto;
import com.skhu.cloud.service.Impl.CreateMokServiceImpl;
import com.skhu.cloud.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/version/test")
@Slf4j
public class VersionTestController {

    private final MainService mainService;
    private final CreateMokServiceImpl createMokService;

    @GetMapping("/1")
    public ModelAndView showVersions(String kind){
        ModelAndView mvc = new ModelAndView("/fragments/sidebar");

        List<FolderDiffDto> mockList =  createMokService.returnMokFolderDtoList(kind);

        for(FolderDiffDto f : mockList){
            System.out.println("~!  " +f.getVersionDto().getFileDto().getName());
        }

        mvc.addObject("time", mainService.getTimeList());
        mvc.addObject("code", mainService.getCodeList()); // mok data 로 받음
        mvc.addObject("mockList", mockList);
        mvc.addObject("kind", (kind.equals(Const.FOLDER)) ? Const.FOLDER : Const.FILE);

        return mvc;
    }
}
