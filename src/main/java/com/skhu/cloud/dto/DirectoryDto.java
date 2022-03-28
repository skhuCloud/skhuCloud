package com.skhu.cloud.dto;

import lombok.Builder;
import lombok.Data;

import java.io.File;

@Data
@Builder
public class DirectoryDto {

    // 총 path , 이것을 list 에다가 담아서 넘길 것임
    private String path;

    // 현재 맨 끝단 디렉토리 , 화면에 표시할 이름
    private String nowDirectory;

    public DirectoryDto(String path , String nowDirectory){
        this.path = path;
        this.nowDirectory = nowDirectory;
    }

    // 마지막 요소를 가져오는 method
    public static String getLastComponent(String path){
        return path.substring(path.lastIndexOf("/") + 1);
    }

    // DirectoryDto 로 관리
    public static DirectoryDto createDirectoryDto(String path){
        return DirectoryDto.builder()
                .path(path)
                .nowDirectory(getLastComponent(path))
                .build();
    }
}
