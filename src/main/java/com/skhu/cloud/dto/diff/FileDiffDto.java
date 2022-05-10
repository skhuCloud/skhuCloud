package com.skhu.cloud.dto.diff;

import com.skhu.cloud.dto.FileDto;
import com.skhu.cloud.model.Content;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDiffDto extends FileDto {
    private List<Content> content; // 요기안에 flag 도 들어있음
}
