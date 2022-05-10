package com.skhu.cloud.dto.diff;

import com.skhu.cloud.dto.FileDto;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class FolderDiffDto extends FileDto {
    private FileDto fileDto;
    private Long flag; // 1 = 수정 X, 2 = 수정 O, 3 = 생성, 4 = 삭제
}