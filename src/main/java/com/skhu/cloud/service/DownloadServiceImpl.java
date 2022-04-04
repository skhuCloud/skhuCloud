package com.skhu.cloud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class DownloadServiceImpl implements DownloadService {

    //다운로드 시작 전 zip이 필요한 경우 return true
    @Override
    public boolean before(HttpServletResponse httpServletResponse,Queue<String> que_path) {
        if(que_path.size() == 1){ // 애초에 queue size 가 1인 경우는 zip 이 필요 없는 경우가 있음
            String path = que_path.peek(); //queue 에 있는 것을 빼면은 안된다 , 그냥 안에 들어있는 것만 확인해야함
            File file = new File(path);
            // que 사이즈 1 &&  파일인 경우
            if(file.isFile()) { // path 를 빼내서 , 해당 파일이 File 인지 확인하고
                que_path.clear(); // queue 를 비운다.
                downloadOne(httpServletResponse,file); // 하나만 다운로드 받는 로직으로 pass
                return false;
            }else {
                // que 사이즈 1 &&  폴더인 경우
                file.delete(); // file 삭제하고
                zipFile(httpServletResponse,que_path); // zip으로 진행
                return true;
            }
        }
        //파일 2개 이상 -> zipFile()
        zipFile(httpServletResponse,que_path);

        return true;
    }


    //단일 파일 다운로드
    @Override
    public void downloadOne(HttpServletResponse httpServletResponse, File file){

        try(FileInputStream fis = new FileInputStream(file);
            OutputStream out = httpServletResponse.getOutputStream()) {

            int read;
            byte[] buffer = new byte[1024];

            while ((read = fis.read(buffer)) >= 0){
                out.write(buffer,0,read); // 그래서 read 만큼 작성한다.
            }

            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + file.getName());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    //다중 팡리 및 폴더 압축 진행
    @Override
    public void zipFile(HttpServletResponse httpServletResponse, Queue<String> que_path){
        System.out.println("파일 압축 중 ,,,,,,,,,,,,,,");

        // 아얘 여기서 setHeader 함
        httpServletResponse.setHeader("Content-Disposition","attachment; filename="+
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")) +".zip"); // 현재의 시간을 , zipfile name 으로 지정

        ZipOutputStream zipOut = null; // 일단 ZipOutputStream 에 null 을 넣고 시작함 , 나중에 zipOut == null 인지 아닌지 확인하는 로직이 존재해서

        try {
            zipOut = new ZipOutputStream(httpServletResponse.getOutputStream()); // httpServletResponse OutputStream 을 ZipOutputStream 으로 주입 , 그러면 압축한 내용을 Servlet 에다가 담을 수 있음

            while (!que_path.isEmpty()) { // que_path 가 이제 없을 때 까지 , 남은 path
                String path = que_path.poll(); // path 들 뺌
                File file = new File(path); // 객체로 만들어주고
                String rootAbsolutePath = file.getAbsolutePath(); // 해당 파일의 절대경로를 받는다.

                if (file.isFile()) {
                    addFile(file, zipOut, file.getName()); // file 이 file 일 때에는 , file path , 해당 file , file path 를 넘긴다.
                    continue;
                }
                Queue<File> queue = new LinkedList<>();
                queue.add(file); // 이제 디렉토리인 경우에는 queue 에다가 , 해당 file(디렉토리) 를 넣고 진행

                while (!queue.isEmpty()) { // queue 가 빌 때 까지
                    File folder = queue.poll();
                    for (File subFile : folder.listFiles()) { // folder 아래에 file 들을 다 받음
                        String subfileAbsolutePath = subFile.getAbsolutePath();  ///Users/jeunning/myDownloadTest/subFolder
                        String relativePath = subfileAbsolutePath.replace(rootAbsolutePath, file.getName());  //myDownloadTest/subFolder
                        // 절대 주소로 남기는 이유는 , 이렇게 안하면 , zip 으로 압축할 때 , 처음 경로부터 즉 , Users 부터 쫘르륵 생김 , 근데 그게 아니라 myDownloadTest 만 받아야 하는 것이니까 , 상대주소로 변경해준다.
                        if (subFile.isDirectory()) { // subFile 이 directory 이면 , queue 에다가 추가해주고 , addFolder 를 해준다
                            queue.add(subFile);
                            addFolder(zipOut, relativePath); // 그래서 여기 relativePath를 주면 addFolder 에서 zip 해줌
                        } else addFile(subFile, zipOut, relativePath); // subFile 이 file 이면 바로 addFile 해주고
                    }
                }
            }
            zipOut.closeEntry();
            zipOut.close();
        } catch (IOException e) {
            e.printStackTrace(); // zipOut 도 , close 조금 더 단순화 시킬 수 있을 것 같은데 아쉽다.
            try { if(zipOut != null) zipOut.closeEntry();} catch (IOException e1) {e1.printStackTrace();}
            try { if(zipOut != null) zipOut.close();} catch (IOException e2) {e2.printStackTrace();}
        }
    }

    @Override
    public void addFolder(ZipOutputStream zipOut, String relativePath) {
        relativePath = relativePath.endsWith("/")? relativePath : relativePath + "/";

        try {
            zipOut.putNextEntry(new ZipEntry(relativePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addFile(File subFile, ZipOutputStream zipOut, String relativePath){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(subFile);

            zipOut.putNextEntry(new ZipEntry(relativePath));

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zipOut.write(buffer, 0, length);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try { if(fis != null) fis.close();} catch (IOException e) {e.printStackTrace();}
        }
    }


}
