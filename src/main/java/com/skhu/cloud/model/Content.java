package com.skhu.cloud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Content { // 해당 flag 와 대응되는 content 를 담음, fileDiff 를 위해서 존재하는 객체
    private Long flag;
    private String content;
}