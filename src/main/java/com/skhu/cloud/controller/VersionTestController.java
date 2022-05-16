package com.skhu.cloud.controller;

import com.skhu.cloud.dto.diff.FolderDiffDto;
import com.skhu.cloud.service.Impl.CreateMokServiceImpl;
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

    private final CreateMokServiceImpl createMokService;

    @GetMapping("/1")
    public ModelAndView showVersions(String kind){
        log.info(" 시작 ㄴㄹㅇㄹ");
        ModelAndView mv = new ModelAndView("/fragments/sidebar");
        List<FolderDiffDto> mockList =  createMokService.createMokFolderDtoList();

        for(FolderDiffDto f : mockList){
            System.out.println("~!  " +f.getFileDto().getName());
        }

        System.out.println(kind);

        mv.addObject("mockList",mockList);
        mv.addObject("kind", kind);

        return mv;
    }
}
