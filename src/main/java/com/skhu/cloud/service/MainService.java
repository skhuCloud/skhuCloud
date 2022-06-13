package com.skhu.cloud.service;

import com.skhu.cloud.dto.DirectoryDto;
import com.skhu.cloud.dto.FileDto;
import com.skhu.cloud.dto.version.FileVersionDto;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Service
public interface MainService {

    // 현재 디렉토리까지의 목록을 가져와 줄 method 가 필요함
    List<DirectoryDto> getDirectoryList(String path);

    // Path 를 이용하여 FileDtoList 를 만들고 정렬을 진행한 후 반환
    List<FileDto> createFileDtoList(String path) throws IOException;

    List<FileDto> pagingFileDtoList(List<FileDto> fileDtoList, Long pageNumber) throws IOException;
    // 해당 폴더 혹은 파일이 무엇인지 알 수 있어야 함
    boolean isDirectory(String path);

    // file 명을 반환하는 메소드
    String getComponentName(String path);

    // 파일을 읽는 method
    String readFile(String path) throws IOException;

    // mvc 에 directoryList , fileDtoList를 등록해주는 method
    void mvcAddObject(ModelAndView mvc, List<DirectoryDto> directoryList, List<FileDto> fileDtoList);

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
