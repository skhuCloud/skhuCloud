package com.skhu.cloud.service;

import com.skhu.cloud.dto.DirectoryDto;
import com.skhu.cloud.dto.FileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
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
    public List<FileDto> createFileDtoList(String path) {
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
        FileInputStream fileInputStream = new FileInputStream(path);

        StringBuilder sb = new StringBuilder();

        while (true) {
            int c = fileInputStream.read();
            if (c == -1) break;
            sb.append((char) c);
        }

        return sb.toString();
    }

    @Override
    public void mvcAddObject(ModelAndView mvc, List<DirectoryDto> directoryList, List<FileDto> fileDtoList) {
        mvc.addObject("directoryList" , directoryList);
        mvc.addObject("fileList" , fileDtoList);
    }
}
