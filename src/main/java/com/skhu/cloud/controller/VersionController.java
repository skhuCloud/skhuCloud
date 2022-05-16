package com.skhu.cloud.controller;

import com.skhu.cloud.dto.diff.FileDiffDto;
import com.skhu.cloud.service.CreateMokService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("version")
public class VersionController {

    private final CreateMokService createMokService;

    @GetMapping("histories")
    public ModelAndView inquiryHistory(String key) throws Exception { // history 를 검색할 수 있는 기능
        ModelAndView mvc = new ModelAndView("folderVersion");

        mvc.addObject("main", key);
        mvc.addObject("folderList", createMokService.returnMokFolderDtoList(key)); // 일단은 그냥 다 폴더로

        return mvc;
    }

    @GetMapping("folder")
    public ModelAndView inquiryFolderVersion(String name) { // folder version 조회
        ModelAndView mvc = new ModelAndView("folderVersion");

        mvc.addObject("main", name);
        mvc.addObject("folderList", createMokService.returnMokFolderDtoList("folderVersion"));

        return mvc;
    }

    @GetMapping("folder/diff")
    public ModelAndView inquiryFolderDiffVersion(String name) { // folder diff version 조회
        ModelAndView mvc = new ModelAndView("folderVersion");

        mvc.addObject("main", name);
        mvc.addObject("folderList", createMokService.returnMokFolderDiffDtoList());

        return mvc;
    }

    @GetMapping("file")
    public ModelAndView inquiryFileVersion(String name) { // file version 조회
        ModelAndView mvc = new ModelAndView("filecontent");

        mvc.addObject("diff", false);
        mvc.addObject("title", name);

        FileDiffDto fileDiffDto = createMokService.returnMokFileDto();

        StringBuilder sb = new StringBuilder();
        fileDiffDto.getContent().stream().forEach(s -> sb.append(s.getContent()));
        mvc.addObject("content", sb.toString());

        return mvc;
    }

    @GetMapping("file/diff")
    public ModelAndView inquiryFileDiffVersion(String name) { // file diff version 조회
        ModelAndView mvc = new ModelAndView("filecontent");

        List<FileDiffDto> diff = createMokService.returnMokFileDiffDto();

        mvc.addObject("diff", true);
        mvc.addObject("title", name);

        List<String> content1 = new ArrayList();
        List<String> content2 = new ArrayList();

        diff.get(0).getContent().stream()
                .forEach(s -> {
                    content1.add(Long.toString(s.getFlag()));
                    content1.add(s.getContent()
                            .replaceAll("\n", "")
                            .replaceAll("\r", ""));
                });
        diff.get(1).getContent().stream()
                .forEach(s -> {
                    content2.add(Long.toString(s.getFlag()));
                    content2.add(s.getContent()
                            .replaceAll("\n", "")
                            .replaceAll("\r", ""));
                }); // content1, 2 생성

        mvc.addObject("content1", content1);
        mvc.addObject("content2", content2);

        return mvc;
    }
}
