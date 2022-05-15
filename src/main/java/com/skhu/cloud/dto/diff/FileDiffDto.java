package com.skhu.cloud.dto.diff;

import com.skhu.cloud.dto.version.VersionDto;
import com.skhu.cloud.model.Content;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true) // 부모까지 같이 비교할 수 있도록
@NoArgsConstructor
@AllArgsConstructor
public class FileDiffDto extends VersionDto {
    private VersionDto versionDto;
    private List<Content> content; // 요기안에 flag 도 들어있음, flag 값은 1, 2, 3 이 있음

    public FileDiffDto(List<Content> content) {
        this.content = content;
    }
}
