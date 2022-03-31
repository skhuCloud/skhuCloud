package com.skhu.cloud.service;

import com.skhu.cloud.dto.DirectoryDto;
import com.skhu.cloud.dto.FileDto;
import com.skhu.cloud.dto.VersionDto;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

public interface MainService {

//    // 현재 디렉토리까지의 목록을 가져와 줄 method 가 필요함
    List<DirectoryDto> getDirectoryList(String path);

    // Local 에서 file list들을 가져와 줄 FileDto로 만들어줄 메소드가 필요함
    List<FileDto> createFileDtoList(String path) throws IOException;

    // 해당 폴더 혹은 파일이 무엇인지 알 수 있어야 함
    boolean isDirectory(String path);

    // 파일을 읽는 method
    String readFile(String path) throws IOException;

    // mvc 에 directoryList , fileDtoList를 등록해주는 method
    void mvcAddObject(ModelAndView mvc, List<DirectoryDto> directoryList, List<FileDto> fileDtoList);

    // versionDto 를 반환해주는 getVersionList
    List<VersionDto> getVersionList(String path);

    // 각 versionDto 들의 Time 을 반환해주는 getTimeList
    List<String> getTimeList(List<VersionDto> versionDtoList);

    // 각 versionDto 들의 Code양을 반환해주는 getCodeList
    List<Long> getCodeList(List<VersionDto> versionDtoList);
}
