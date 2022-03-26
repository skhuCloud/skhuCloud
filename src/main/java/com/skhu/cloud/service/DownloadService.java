package com.skhu.cloud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class DownloadService {


    //단일 파일 다운로드









    //다중 파일 .zip 형태로 다운로드
    public void zip(){
        System.out.println("파일 압축 중 ,,,,,,,,,,,,,,");
        String path = "/Users/jeunning/myDownloadTest";
        File file = new File(path);
        File[] listFiles = file.listFiles();

        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;

        try {
            fos = new FileOutputStream("/Users/jeunning/myDownloadTest/zipTest.zip");
            zipOut = new ZipOutputStream(fos);

            for(File fileToZip : listFiles){
                fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int length;
                while((length = fis.read(buffer)) >= 0){
                    zipOut.write(buffer,0,length);
                }

                fis.close();
                zipOut.closeEntry();
            }
            zipOut.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            try { if(fis != null) fis.close();} catch (IOException e1) {System.out.println(e1.getMessage());}
            try { if(zipOut != null) zipOut.closeEntry();} catch (IOException e2){System.out.println(e2.getMessage());}
            try { if(zipOut != null) zipOut.close();} catch (IOException e3) {System.out.println(e3.getMessage());}
            try { if(fos != null)fos.close();} catch (IOException e4) {System.out.println(e4.getMessage());}

        }
    }
}
