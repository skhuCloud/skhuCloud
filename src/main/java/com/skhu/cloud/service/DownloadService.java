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
public class DownloadService {

    //다운로드 시작 전 zip이 필요한 경우 return true
    public boolean before(HttpServletResponse httpServletResponse,Queue<String> que_path) throws IOException {
        if(que_path.size() == 1){
            String path = que_path.peek();
            File file = new File(path);
            // que 사이즈 1 &&  파일인 경우
            if(file.isFile()) {
                que_path.clear();
                downloadOne(httpServletResponse,file);
                return false;
            }else {
                // que 사이즈 1 &&  폴더인 경우
                file.delete();
                zipFile(httpServletResponse,que_path);
                return true;
            }
        }
        //파일 2개 이상 -> zipFile()
        zipFile(httpServletResponse,que_path);

        return true;
    }


    //단일 파일 다운로드
    public void downloadOne(HttpServletResponse httpServletResponse, File file){
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

            //attachment 로 설정함으로써 해당 데이터를 수신받은 브라우저가 팡링르 저장 또는 다른이름으로 저장 여부를 설정하게 할 수 있다.
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + file.getName());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            //fis,out close 로직 작성하기
        }
    }


    //다중 팡리 및 폴더 압축 진행
    public void zipFile(HttpServletResponse httpServletResponse, Queue<String> que_path) throws IOException {
        System.out.println("파일 압축 중 ,,,,,,,,,,,,,,");

        httpServletResponse.setHeader("Content-Disposition","attachment; filename="+
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")) +".zip");

        ZipOutputStream zipOut = new ZipOutputStream(httpServletResponse.getOutputStream());

        while(!que_path.isEmpty()){
            String path = que_path.poll();
            File file = new File(path);
            String rootAbsolutePath = file.getAbsolutePath();

            if(file.isFile()) {
                addFile(file,zipOut,file.getName());
                continue;
            }
            Queue<File> queue = new LinkedList<>();
            queue.add(file);

            while (!queue.isEmpty()){
                File folder = queue.poll();
                for(File subFile : folder.listFiles()){
                    String subfileAbsolutePath = subFile.getAbsolutePath();  ///Users/jeunning/myDownloadTest/subFolder
                    String relativePath = subfileAbsolutePath.replace(rootAbsolutePath,file.getName());  //myDownloadTest/subFolder

                    if(subFile.isDirectory()) {
                        queue.add(subFile);
                        addFolder(zipOut,relativePath);
                    }
                    else addFile(subFile, zipOut, relativePath);
                }
            }
        }

        zipOut.closeEntry();
        zipOut.close();
    }

    public void addFolder(ZipOutputStream zipOut, String relativePath) throws IOException {
        relativePath = relativePath.endsWith("/")?relativePath : relativePath + "/";
        zipOut.putNextEntry(new ZipEntry(relativePath));
    }

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
