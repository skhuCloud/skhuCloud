package com.skhu.cloud.controller;

import com.skhu.cloud.service.DownloadServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Controller
@Slf4j
@RequiredArgsConstructor
//@RequestMapping("/download")
public class DownloadController {

    private final DownloadServiceImpl downloadService;

    @GetMapping("/download/checked")
    public void downloadChecked(@RequestParam(value = "checkedFiles",required = false)List<String> checkedFiles, HttpServletResponse httpServletResponse)  {
        System.out.println("실행 됨~~~~000~");
        System.out.println(checkedFiles);
        try {
            Queue<String> que_checked = new LinkedList<>();
            for(String s : checkedFiles) {
                que_checked.add(s);
                System.out.println("~~~   " + s);
            }

            //before() zip 의 필요 유뮤를 리턴.
            boolean toZip = downloadService.before(httpServletResponse,que_checked);
            if(toZip){
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                httpServletResponse.setContentType("application/zip");
            }
        } catch (NullPointerException e){
            System.out.println("선택된 파일이 없습니다.");
        }
    }

}
