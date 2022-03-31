package com.skhu.cloud.controller;

import com.skhu.cloud.service.DownloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.zip.ZipOutputStream;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/download")
public class DownloadController {

    private final DownloadService downloadService;

    // file download
    // 해당 path 만 동적으로 다룰 수 있으면 될 듯
    // 근데 이제 한번에 여러개의 path를 어떻게 넘기냐는 것이다.
    @GetMapping("")
    public void download(HttpServletResponse httpServletResponse) {
        String path = "/Users/jaeyeonkim/testDirectory/test1.txt"; //download 할 로컬파일의 경로
        downloadService.downloadOne(httpServletResponse,path);
    }



    //.zip 형태로 다운로드
    @GetMapping("zip")
    public void downloadZip(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.setContentType("application/zip");

        String path = "/Users/jaeyeonkim/testDirectory"; //압축할 파일 위치

        // 확실하게 이 zipName 에 문제가 있는 듯하다 , 즉 zipName의 문제는 정확히 말하면 아니고 , donwloadService.zipFile 의 문제이다.
        downloadService.zipFile(httpServletResponse,path);
    }
}
