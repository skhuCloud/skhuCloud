package com.skhu.cloud.service;

import com.skhu.cloud.dto.FileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    @Override
    public List<String> getDirectoryList(String path) {
        List<String> list = new ArrayList<>();

        while (!path.isBlank()) {
            String directory;

            if (path.lastIndexOf("/") == path.indexOf("/")) { // 하나만 남은 것
                directory = path.substring(1);
                path = "";
            } else {
                directory = path.substring(path.indexOf("/") + 1, path.indexOf("/", 1));
                path = path.substring(path.indexOf("/", 1));
            }

            list.add(directory);
        }

        return list;
    } // list 를 return

    @Override
    public List<FileDto> createFileDtoList(String path) {
        // 해당 path 를 받으면 해당 path 아래에 모든 것들을 createFileDto 에다가 넘겨서 fileDtoList 로 만든다음 반환
        File file = new File(path);

        File[] files = file.listFiles();
        List<FileDto> list = new ArrayList<>();

        if (file != null) {
            for (File innerFile : files) {
                list.add(createFileDto(innerFile));
            }
        }

        return list;
    }

    @Override
    public FileDto createFileDto(File file) {
        // file 을 넘겨받으면 fileDto 로 만들어서 넘겨
        return FileDto.builder()
                .name(file.getName())
                .modifiedTime(Instant.ofEpochMilli(file.lastModified())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .kind(file.isDirectory() ? "폴더" : "파일")
                .size(file.length())
                .path(file.getPath())
                .build();
    }

    @Override
    public String returnPath(String path, Long index) {
        File[] files = new File(path).listFiles();
        return files[index.intValue()].getPath();
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
    public void mvcAddObject(ModelAndView mvc, List<String> directoryList, List<FileDto> fileDtoList) {
        mvc.addObject("directoryList" , directoryList);
        mvc.addObject("fileList" , fileDtoList);
    }

    @Override
    public String moveDirectory(Long index , List<String> pathList){
        StringBuilder sb = new StringBuilder("/");

        for(Long i = 0L; i <= index; i++){ // 같은 거 두번 눌렀을 때 에러남.. 어떻게 해결해야 할까?
            sb.append(pathList.get(i.intValue()) + "/");
        }

        String result = sb.toString();
        return result.substring(0 , result.length() - 1);
    }
}
