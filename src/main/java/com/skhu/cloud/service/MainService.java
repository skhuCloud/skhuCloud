package com.skhu.cloud.service;

import com.skhu.cloud.dto.DirectoryDto;
import com.skhu.cloud.dto.FileDto;
import com.skhu.cloud.dto.FileVersionDto;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

public interface MainService {

    // 현재 디렉토리까지의 목록을 가져와 줄 method 가 필요함
    List<DirectoryDto> getDirectoryList(String path);

    // Local 에서 fileDto kind 가 파일인 것의 List를 반환
    List<FileDto> createFileDtoList(String path) throws IOException;

    // Local 에서 fileDto kind 가 폴더인 것의 List를 반환
    List<FileDto> createFolderDtoList(String path) throws IOException;

    // 해당 폴더 혹은 파일이 무엇인지 알 수 있어야 함
    boolean isDirectory(String path);

    String getComponentName(String path);

    // 파일을 읽는 method
    String readFile(String path) throws IOException;

    // mvc 에 directoryList , fileDtoList를 등록해주는 method
    void mvcAddObject(ModelAndView mvc, List<DirectoryDto> directoryList,List<FileDto> folderDtoList, List<FileDto> fileDtoList);

    // "/files" 에 mvc.addObject 가 너무 많아서 , Service 측으로 옮김
    void filesMvcAddObject(ModelAndView mvc , String extension , List<FileVersionDto> versionList,
                           List<String> time , List<Long> code , String path , Long index , String title) throws IOException;

    // versionDto 를 반환해주는 getVersionList
    List<FileVersionDto> getVersionList(String path);

    // 각 versionDto 들의 Time 을 반환해주는 getTimeList
    List<String> getTimeList(List<FileVersionDto> fileVersionDtoList);

    // 각 versionDto 들의 Code양을 반환해주는 getCodeList
    List<Long> getCodeList(List<FileVersionDto> fileVersionDtoList);

    // "/version" controller 에서 mvc object 에다가 content 주입
    void versionMvcAddObject(ModelAndView mvc , String extension , List<FileVersionDto> versionList,
                           List<String> time , List<Long> code , String path , Long index , String title) throws IOException;
}
