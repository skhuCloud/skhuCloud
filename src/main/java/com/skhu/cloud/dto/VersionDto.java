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

    // getTime 만 수정하여서 , html 에 표기할 시간을 수정
    public String getTime(){
        // number + "" 을 통해서 string 으로 변환
        String minute = (this.time.getMinute() + "").length() == 1 ? "0" + this.time.getMinute() : this.time.getMinute() + "";
        String second = (this.time.getSecond() + "").length() == 1 ? "0" + this.time.getSecond() : this.time.getSecond() + "";
        return this.time.getMonthValue() + "월 " + this.time.getDayOfMonth() + "일 "
                + this.time.getHour() + ":" + minute + ":" + second;
    }

    // 살짝의 연산을 통해서 가시성 업그레이드
    public String getTimeToString(){
        String minute = (this.time.getMinute() + "").length() == 1 ? "0" + this.time.getMinute() : this.time.getMinute() + "";
        String second = (this.time.getSecond() + "").length() == 1 ? "0" + this.time.getSecond() : this.time.getSecond() + "";
        return this.time.getHour() + ":" + minute + ":" + second;
    }
}
