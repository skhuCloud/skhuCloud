package com.skhu.cloud.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDto {

    // fileName
    private String name;

    // 마지막 수정시간 (문자열로 나타냄)
    private String modifiedTime;

    // 파일의 크기 KB 기준임
    private String size;

    // 파일 종류 ex ) 디렉토리 혹은 extension
    private String kind;

    private String path;

    private String imageUrl;

    // 확장자 따는 method , 근데 . 으로 시작하는 파일들은 숨김파일인데 , 그런 것들은 어떻게 해야 할까?
    public static String getExtension(String path){
        return path.substring(path.lastIndexOf(".") + 1);
    }

    // 해당 파일의 size를 받고 , byte , kb , mb , gb , tb 등등으로 변환시킬 것임
    public static String sizeConvert(double size) {
        HashMap<Integer , String> map = new HashMap<>();

        map.put(0 , "B");
        map.put(1 , "KB");
        map.put(2 , "MB");
        map.put(3 , "GB");
        map.put(4 , "TB");
        map.put(5 , "PB");
        map.put(6 , "EB");

        int index = 0;
        while (size >= 1024) { // size 가 1024 보다 작을 때까지 진행해야함 , 그래야지 더 나눌 수가 없으니까
            index++;
            size /= 1024d;
        }

        return String.format("%.1f" , size) + map.get(index); // 한자리 까지만
    }

    public static String mappingImageUrl(File file) {
        HashMap<String, String> map = new HashMap<>();

        // 기본
        map.put("folder", "/static/images/folder.png");
        map.put("file", "/static/images/file.png");

        // 추가
        map.put("java", "/static/images/java.png");
        map.put("css", "/static/images/css.png");
        map.put("html", "/static/images/html.png");
        map.put("js", "/static/images/js.png");
        map.put("xml", "/static/images/xml.png");

        String key = "";

        if (file.isDirectory()) { // folder 이면
            key = "folder";
        } else { // file 이면
            key = getExtension(file.getName());

            if (!map.containsKey(key)) { // Extension 이 만일 HashMap 에 없는 extension이면 그냥 file 로 표시
                key = "file";
            }
        }

        return map.get(key);
    }

    public static String modifiedTime(Long lastModified) {
        // 년 , 월 , 일 , 시 , 분 , 초 로 나타내면 된다.
        LocalDateTime time = Instant.ofEpochMilli(lastModified)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return time.getYear() + "년 " + time.getMonthValue() + "월 " + time.getDayOfMonth() + "일 "
                + time.getHour() + "시 " + time.getMinute() + "분 " + time.getSecond() +"초 ";
    }

    // 해당 method 가 static method 이기 떄문에 , getExtension도 static method 로 선언을 해야함
    public static FileDto createFileDto(File file) throws IOException {
        return FileDto.builder()
                .name(file.getName())
                .modifiedTime(modifiedTime(file.lastModified()))
                .kind(file.isDirectory() ? "폴더" : getExtension(file.getPath()) + " 파일")
                .size(sizeConvert(Files.size(Paths.get(file.getPath())))) // 사이즈를 byte로 받기 위한 연산
                .path(file.getPath())
                .imageUrl(mappingImageUrl(file))
                .build();
    }
}
