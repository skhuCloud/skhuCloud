package com.skhu.cloud.dto.diff;

import com.skhu.cloud.dto.version.VersionDto;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class FolderDiffDto extends VersionDto {
    private VersionDto versionDto;
    private Long flag; // 1 = 수정 X, 2 = 수정 O, 3 = 생성, 4 = 삭제

    public FolderDiffDto(Long flag) {
        this.flag = flag;
    }
}