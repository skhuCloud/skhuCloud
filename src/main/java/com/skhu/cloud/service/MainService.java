package com.skhu.cloud.service;

import com.skhu.cloud.dto.DirectoryDto;
import com.skhu.cloud.dto.FileDto;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Service
public interface MainService {

    // 현재 경로를 파싱하여서, Parent 들의 경로들도 다 얻어냄
    List<DirectoryDto> getDirectoryList(String path);

    // 경로를 이용하여 하위 파일들을 얻어오고 FileDtoList 로 가공한 뒤 주어진 정렬 조건으로 정렬을 한 뒤 반환
    List<FileDto> createFileDtoList(String path, String sortBy, String direction) throws IOException;

    // 주어진 List를 주어진 Page 에 맞게 Paging 해서 반환
    List<FileDto> pagingFileDtoList(List<FileDto> fileDtoList, Long pageNumber) throws IOException;

    // 페이지 네이션에 필요한 정보들을 계산하여 반환
    Long[] calculatePageNumber(List<FileDto> fileDtoList, Long jump, Long pageNumber);

    // path, key(검색어) 가 주어지면 하위 디렉토리에서 이것과 유사한 것들을 전부 찾아온다.
    List<FileDto> findSubFile(String path, String[] key) throws IOException;

    // List 를 정렬해주는 메소드
    List<FileDto> sortByFileDtoList(List<FileDto> fileDtoList, String sortBy, String direction);

    // file 명을 반환하는 메소드
    String getComponentName(String path);

    // 운영체제에 맞춰 Root Path 를 반환한다.
    String getRootPath();

    // 파일을 읽는 method
    String readFile(String path) throws IOException;

    // mvc 에 directoryList , fileDtoList를 등록해주는 method
    void directoriesAddObject(ModelAndView mvc, List<DirectoryDto> directoryList, List<FileDto> fileDtoList, String path,
                              Long Page, Long startNumber, Long endNumber, String sortBy, String direction);

    // file 을 불러올 때 필요한 정보들을 등록해주는 method
    void filesMvcAddObject(ModelAndView mvc , boolean diff, String extension,
                           String path , String title) throws IOException;

    void searchMvcAddObject(ModelAndView mvc, String nowPath, List<FileDto> fileList);

    // 각 versionDto 들의 Time 을 반환해주는 getTimeList
    List<String> getTimeList();

    // 각 versionDto 들의 Code양을 반환해주는 getCodeList
    List<Long> getCodeList();
}
