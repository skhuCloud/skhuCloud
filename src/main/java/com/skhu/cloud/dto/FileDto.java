package com.skhu.cloud.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Builder
public class FileDto {

    // fileName
    private String name;

    // 마지막 수정시간
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedTime;

    // 파일의 크기 KB 기준임
    private Long size;

    // 파일 종류 ex ) 디렉토리 혹은 extension
    private String kind;

    private String path;

    public FileDto(String name , LocalDateTime modifiedTime , Long size , String kind , String path){
        this.name = name;
        this.modifiedTime = modifiedTime;
        this.size = size;
        this.kind = kind;
        this.path = path;
    }

    // 확장자 따는 method , 근데 . 으로 시작하는 파일들은 숨김파일인데 , 그런 것들은 어떻게 해야 할까?
    public static String getExtension(String path){
        return path.substring(path.lastIndexOf(".") + 1);
    }

    // 해당 method 가 static method 이기 떄문에 , getExtension도 static method 로 선언을 해야함
    public static FileDto createFileDto(File file){
        return FileDto.builder()
                .name(file.getName())
                .modifiedTime(Instant.ofEpochMilli(file.lastModified())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .kind(file.isDirectory() ? "폴더" : getExtension(file.getPath()) + " 파일")
                .size(file.length())
                .path(file.getPath())
                .build();
    }
}
