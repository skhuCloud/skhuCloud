package com.skhu.cloud.service;

import com.skhu.cloud.dto.FileDto;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface MainService {

    // 현재 디렉토리까지의 목록을 가져와 줄 method 가 필요함
    List<String> getDirectoryList(String path);

    // Local 에서 file list들을 가져와 줄 FileDto로 만들어줄 메소드가 필요함
    List<FileDto> createFileDtoList(String path);

    // FileDto 로 만들어주는 method
    FileDto createFileDto(File file);

    String returnPath(String path , Long index);

    // 이제는 위의 디렉토리를 누르면 이동하는 게 필요함
    String moveDirectory(Long index , List<String> pathList);

    // 해당 폴더 혹은 파일이 무엇인지 알 수 있어야 함
    boolean isDirectory(String path);

    String readFile(String path) throws IOException;

    void mvcAddObject(ModelAndView mvc , List<String> directoryList , List<FileDto> fileDtoList);
}
