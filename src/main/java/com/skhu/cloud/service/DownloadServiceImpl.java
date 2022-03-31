package com.skhu.cloud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class DownloadServiceImpl implements DownloadService{

    //***********   단일 파일 다운로드  *********
    @Override
    public void downloadOne(HttpServletResponse httpServletResponse, String path){
        File file = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            OutputStream out = null;
            out = httpServletResponse.getOutputStream();

            //파일 작성하기
            int read;
            byte[] buffer = new byte[1024];

            while ((read = fis.read(buffer)) >= 0){
                out.write(buffer,0,read);
            }

            //다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더 및 폴더 저장명 지정
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + file.getName()); // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    //폴더 압축 진행
    @Override
    public void zipFile(HttpServletResponse httpServletResponse, String path) throws IOException {
        System.out.println("파일 압축 중 ,,,,,,,,,,,,,,");
        ZipOutputStream zipOut = new ZipOutputStream(httpServletResponse.getOutputStream());
        File file = new File(path);
        String rootAbsolutePath = file.getAbsolutePath();

        Queue<File> queue = new LinkedList<>();
        queue.add(file);

        while (!queue.isEmpty()){
            File folder = queue.poll();
            for(File subFile : folder.listFiles()){
                String subfileAbsolutePath = subFile.getAbsolutePath();  ///Users/jeunning/myDownloadTest/subFolder
//                System.out.println("file 이름 : " + file.getName());
                String relativePath = subfileAbsolutePath.replace(rootAbsolutePath,file.getName());  //myDownloadTest/subFolder

                if(subFile.isDirectory()) {
                    queue.add(subFile);
                    System.out.println("   " + file.getName());
                    System.out.println("rootAbsolutePath : " + rootAbsolutePath);
                    System.out.println("subfileAbsolutePath : " + subfileAbsolutePath);
                    System.out.println("relative path : " + relativePath );

                    addFolder(zipOut,relativePath);
                }
                else addFile(subFile, zipOut, relativePath);
            }
        }
        zipOut.closeEntry();

        // 이전에 zipOut 객체가 완전히 close 되고 다운로드 되는 zip의 이름을 바꾸려고 하니 에러가 났었떤 것이였음 , 그래서 close 호출 전에 setHeader를 해주었더니 해결
        httpServletResponse.setHeader("Content-Disposition","attachment; filename="+file.getName() + ".zip");

        zipOut.close();
    }

    @Override
    public void addFolder(ZipOutputStream zipOut, String relativePath) throws IOException {
        relativePath = relativePath.endsWith("/")?relativePath : relativePath + "/";
        zipOut.putNextEntry(new ZipEntry(relativePath));
    }

    @Override
    public void addFile(File subFile, ZipOutputStream zipOut, String relativePath) throws IOException {
        FileInputStream fis = new FileInputStream(subFile);
        zipOut.putNextEntry(new ZipEntry(relativePath));

        byte[]buffer = new byte[1024];
        int length;
        while((length = fis.read(buffer)) >= 0){
            zipOut.write(buffer,0,length);
        }
        fis.close();
    }


}
