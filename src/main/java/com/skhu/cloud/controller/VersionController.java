package com.skhu.cloud.controller;

import com.skhu.cloud.constant.Const;
import com.skhu.cloud.dto.diff.FileDiffDto;
import com.skhu.cloud.dto.diff.FolderDiffDto;
import com.skhu.cloud.service.CreateMokService;
import com.skhu.cloud.service.Impl.CreateMokServiceImpl;
import com.skhu.cloud.service.MainService;
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
    private final MainService mainService;
    private final CreateMokService createMokService;

    @GetMapping("histories")
    public ModelAndView inquiryHistory(String key) throws Exception { // history 를 검색할 수 있는 기능
        ModelAndView mvc = new ModelAndView("folderVersion");

        mvc.addObject("main", key);
        mvc.addObject("diff", false);
        mvc.addObject("fileList", createMokService.returnMokFolderDtoList(key)); // 일단은 그냥 다 폴더로

        return mvc;
    }

    @GetMapping("folder")
    public ModelAndView inquiryFolderVersion(String name) { // folder version 조회
        ModelAndView mvc = new ModelAndView("folderVersion");

        mvc.addObject("main", name);
        mvc.addObject("diff", false);
        mvc.addObject("fileList", createMokService.returnMokFolderDtoList("folderVersion"));

        return mvc;
    }

    @GetMapping("folder/diff")
    public ModelAndView inquiryFolderDiffVersion(String name) { // folder diff version 조회
        ModelAndView mvc = new ModelAndView("folderVersion");

        mvc.addObject("main", name);
        mvc.addObject("diff", true);
        mvc.addObject("fileList", createMokService.returnMokFolderDiffDtoList());

        return mvc;
    }

    @GetMapping("file")
    public ModelAndView inquiryFileVersion(String name) { // file version 조회
        ModelAndView mvc = new ModelAndView("filecontent");
        FileDiffDto fileDiffDto = createMokService.returnMokFileDto();

        mvc.addObject("diff", false);
        mvc.addObject("title", name);
        mvc.addObject("modified", fileDiffDto.getVersionDto().getFileDto().getModifiedTime());

        StringBuilder sb = new StringBuilder();
        fileDiffDto.getContent().forEach(s -> sb.append(s.getContent()));
        mvc.addObject("content", sb.toString());
        mvc.addObject("extension", "java");

        return mvc;
    }

    @GetMapping("file/diff")
    public ModelAndView inquiryFileDiffVersion(String name) { // file diff version 조회
        ModelAndView mvc = new ModelAndView("filecontent");

        List<FileDiffDto> diff = createMokService.returnMokFileDiffDto();
        FileDiffDto first = diff.get(0);
        FileDiffDto second = diff.get(1);

        mvc.addObject("diff", true);
        mvc.addObject("title", name);
        mvc.addObject("firstModified", first.getVersionDto().getFileDto().getModifiedTime());
        mvc.addObject("secondModified", first.getVersionDto().getFileDto().getModifiedTime());

        List<String> content1 = new ArrayList();
        List<String> content2 = new ArrayList();

        first.getContent()
                .forEach(s -> {
                    content1.add(Long.toString(s.getFlag()));
                    content1.add(s.getContent()
                            .replaceAll("\n", "")
                            .replaceAll("\r", ""));
                });
        second.getContent()
                .forEach(s -> {
                    content2.add(Long.toString(s.getFlag()));
                    content2.add(s.getContent()
                            .replaceAll("\n", "")
                            .replaceAll("\r", "")); // 여러 줄 String 을 js 가 지원하지 않아, 줄바꿈 문자를 다 없앴음
                }); // content1, 2 생성

        mvc.addObject("content1", content1);
        mvc.addObject("content2", content2);

        return mvc;
    }

    @GetMapping("/side")
    public ModelAndView showVersions(String kind){ // side bar 를 받아오는 api
        ModelAndView mvc = new ModelAndView("/fragments/sidebar");

        List<FolderDiffDto> mockList =  createMokService.returnMokFolderDtoList(kind);

        mvc.addObject("time", mainService.getTimeList());
        mvc.addObject("code", mainService.getCodeList()); // mok data 로 받음
        mvc.addObject("mockList", mockList);
        mvc.addObject("kind", (kind.equals(Const.FOLDER)) ? "folder" : "file");

        return mvc;
    }
}
