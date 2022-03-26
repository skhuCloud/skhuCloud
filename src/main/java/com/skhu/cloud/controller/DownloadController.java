package com.skhu.cloud.controller;

import com.skhu.cloud.service.DownloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;

@Controller
@Slf4j
@RequiredArgsConstructor
//@RequestMapping("/download")
public class DownloadController {

    private final DownloadService downloadService;

    //file download
    @GetMapping("/download")
    public void download(HttpServletResponse httpServletResponse) throws Exception {
        try {
            String path = "/Users/jeunning/myDownloadTest/mydownloadTest.txt"; //download 할 로컬파일의 경로

            File file = new File(path);
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + file.getName()); // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더


            FileInputStream fis = new FileInputStream(path); //파일 읽어오기
            OutputStream out = httpServletResponse.getOutputStream();

            int read = 0;
            byte[] buffer = new byte[1024];

            while ((read = fis.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }catch (Exception e){
            throw new Exception("download error !!!");
         }
    }



    //.zip 형태로 다운로드
    @GetMapping("download/zip")
    public void downloadZip(){
        downloadService.zip();
    }


}
