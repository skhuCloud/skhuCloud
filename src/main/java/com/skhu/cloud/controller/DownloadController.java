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
//@RequestMapping("/download")
public class DownloadController {

    private final DownloadService downloadService;

    //file download
    @GetMapping("/download")
    public void download(HttpServletResponse httpServletResponse) {
        String path = "/Users/jeunning/myDownloadTest/mydownloadTest.txt"; //download 할 로컬파일의 경로
        downloadService.downloadOne(httpServletResponse,path);

    }



    //.zip 형태로 다운로드
    @GetMapping("download/zip")
    public void downloadZip(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.setContentType("application/zip");

        String path = "/Users/jeunning/myDownloadTest"; //압축할 파일 위치

//        httpServletResponse.setHeader("Content-Disposition","attachment; filename="+"zipTest123.zip");

        String zipName = downloadService.zipFile(httpServletResponse,path);
        System.out.println("~~~~~   " + zipName);
        httpServletResponse.setHeader("Content-Disposition","attachment; filename="+zipName);
    }


}
