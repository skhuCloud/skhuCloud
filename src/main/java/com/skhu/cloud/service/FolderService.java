package com.skhu.cloud.service;

import com.skhu.cloud.dto.FolderVersionDto;
import com.skhu.cloud.entity.FolderVersion;

import java.time.LocalDateTime;
import java.util.List;


public interface FolderService {
    // folder version list 를 얻는 메소드
    List<FolderVersionDto> getFolderVersionDtoList(List<FolderVersion> folderVersionList);

    // parent 변수를 통해서 , 역순으로 경로를 알아내고 , 거기에서 title 을 따내서 , 반환할 예정이다.
    List<String> getDirectoryList(Long parent , LocalDateTime time);

    // getDirectoryList method 에서 사용될 , LocalDateTime 과 , parent 를 가지고 현재에 가장 적합한 파일을 반환하는 역할이다.
    FolderVersion getComponent(Long componentId , LocalDateTime time);

    // getFolderVersionList 를 넘기기전에 , 해당 디렉토리 아래에 가장 적한한 폴더와 파일들을 반환하는 메소드
    List<FolderVersion> getFolderVersionList(Long parent , LocalDateTime time);
}
