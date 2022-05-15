package com.skhu.cloud.controller;

import com.skhu.cloud.service.CreateMokService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("version")
public class VersionController {

    private final CreateMokService createMokService;

    @GetMapping("histories")
    public ModelAndView getHistory(String key, String path) { // history 를 검색할 수 있는 기능
        ModelAndView mvc = new ModelAndView("history");

        mvc.addObject("key", key);
        mvc.addObject("folderList", createMokService.returnMokFolderDtoList(key)); // 일단은 그냥 다 폴더로만
        mvc.addObject("nowPath", path);

        return mvc;
    }
}
