package com.skhu.cloud.service;

import com.skhu.cloud.dto.DirectoryDto;
import com.skhu.cloud.dto.FileDto;
import com.skhu.cloud.dto.FileVersionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainServiceImpl implements MainService {

    @Override
    public List<DirectoryDto> getDirectoryList(String path) {
        List<DirectoryDto> result = new ArrayList<>();
        int index = 0;

        while(true){
            if(path.indexOf("/" , index) == path.lastIndexOf("/")) {
                result.add(DirectoryDto.createDirectoryDto(path));
                break; // 전체를 넘기고 바로 끝
            }

            index = path.indexOf("/" , ++index); // 이러면 다음 / 를 찾을 수 있음
            result.add(DirectoryDto.createDirectoryDto(path.substring(0 , index))); // 이러면 계속해서 다음 것을 찾을 것임
        }

        return result;
    } // list 를 return

    @Override
    public List<FileDto> createFileDtoList(String path) throws IOException{
        // 해당 path 를 받으면 해당 path 아래에 모든 것들을 createFileDto 에다가 넘겨서 fileDtoList 로 만든다음 반환
        File file = new File(path);

        File[] files = file.listFiles();
        List<FileDto> list = new ArrayList<>();

        if (file != null) {
            for (File innerFile : files) {
                list.add(FileDto.createFileDto(innerFile));
            }
        }

        return list;
    }

    @Override
    public boolean isDirectory(String path) {
        return new File(path).isDirectory(); // directory 면 true , 아니면 false 를 반환한다.
    }

    @Override
    public String readFile(String path) throws IOException {
        // 계층적인 방법을 이용하여서 속도 향상 및 인코딩 변경이 가능함
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path) , "UTF-8"));

        StringBuilder sb = new StringBuilder();

        while (true) {
            String string = reader.readLine();
            if (string == null) break;
            sb.append(string + "\r\n"); // 잘 출력된다 , content 로서 넘어갈 때 , 문제가 있는 듯
        }

        return sb.toString();
    }

    @Override
    public void mvcAddObject(ModelAndView mvc, List<DirectoryDto> directoryList, List<FileDto> fileDtoList) {
        mvc.addObject("directoryList" , directoryList);
        mvc.addObject("fileList" , fileDtoList);
    }

    @Override
    public void filesMvcAddObject(ModelAndView mvc , String extension, List<FileVersionDto> versionList,
                                  List<String> time, List<Long> code, String path, Long index , String title) throws IOException{
        // 원래 content 를 직접적으로 넘겼었지만 , versionList 에 담겨있는 content 를 넘기는 식으로 request header to large 문제를 해결 하였음
        mvc.addObject("extension" , extension);
        mvc.addObject("versionList" , versionList);
        mvc.addObject("time" , time);
        mvc.addObject("code" , code);
        mvc.addObject("path" , path);
        mvc.addObject("content" , versionList.get(index.intValue()).getContent());
        mvc.addObject("index" , index);
        mvc.addObject("title" , title);
    }

    @Override
    public String getComponentName(String path){
        // 만약 / 가 없다면 -1 을 반환한다.
        int lastIndex = path.lastIndexOf("/");

        if(lastIndex == -1) return path;
        else return path.substring(lastIndex + 1);
    }

    @Override
    public List<FileVersionDto> getVersionList(String path){
        List<FileVersionDto> result = new ArrayList<>();
        // path 추가(생성자)
        try {
            for (int i = 0; i < 10; i++) {
                if(i == 0) result.add(new FileVersionDto(LocalDateTime.now() , readFile(path)));
                else result.add(new FileVersionDto(LocalDateTime.now(), "안녕하세요" + i));
            }
        } catch(IOException e){ // throws IOException 처리
            log.error("error code : " + e.getMessage());
        }
        return result;
    }

    // 해당 versionDto List 에서 Time 만 빼내서 표현
    @Override
    public List<String> getTimeList(List<FileVersionDto> fileVersionDtoList) {
        List<String> result = new ArrayList<>();
        for(FileVersionDto fileVersionDto : fileVersionDtoList){
            result.add(fileVersionDto.getTimeToString());
        }
        return result;
    }

    // 해당 versionDto List 에서 Code양 만 빼내서 표현
    @Override
    public List<Long> getCodeList(List<FileVersionDto> fileVersionDtoList) {
        List<Long> result = new ArrayList<>();

        // 지금은 코드가 그럴싸 해 보이도록 조금 바꾸었음
        Random random = new Random();

        for(FileVersionDto fileVersionDto : fileVersionDtoList){
            result.add(new Long(fileVersionDto.getContent().length()) + random.nextInt(500));
        }
        return result;
    }
}
