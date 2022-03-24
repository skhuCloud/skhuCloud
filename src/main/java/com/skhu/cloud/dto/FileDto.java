package com.skhu.cloud.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
public class FileDto {

    // fileName
    String name;

    // 마지막 수정시간
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime modifiedTime;

    // 파일의 크기 KB 기준임
    Long size;

    // 파일 종류 ex ) 디렉토리 , 파일
    String kind;

    String path;

    public FileDto(String name , LocalDateTime modifiedTime , Long size , String kind , String path){
        this.name = name;
        this.modifiedTime = modifiedTime;
        this.size = size;
        this.kind = kind;
        this.path = path;
    }
}
