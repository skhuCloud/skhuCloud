package com.skhu.cloud.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VersionDto {

    LocalDateTime time;

    String content;

    // dto list 를 만들 떄에도 그냥 new 연산자 이용해서 만들면 된다.
    public VersionDto(LocalDateTime time , String content){
        this.time = time;
        this.content = content;
    }

    public String getTimeToString(){
        return time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();
    }
}
