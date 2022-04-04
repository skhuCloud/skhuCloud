package com.skhu.cloud.dto;


import java.time.LocalDateTime;

/*
해당 방식을 정리하면 이러하다 , 일단 해당 파일에 대해서 , 수정이 일어나게 되면 , 상위 version 에다가 등록한다.
이렇게 변경이 일어났다고 , 근데 file 에 명시할 필요는 없다 , 즉 DirectoryVersionDto 에다가 등록하면 된다.
그 DirectoryVersionDto 에다가 등록을 하면 되고, 그 다음에 폴더에서 해당 버전을
 */
public class DirectoryVersionDto {

    // 해당 폴더 버전이 등록되었을 때 , 등록된다.
//    LocalDateTime time;
}
