package com.skhu.cloud.dto.diff;

import com.skhu.cloud.dto.FileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FolderDiffDto extends FileDto {

    // version 의 상태를 나타내는 필드
    private Long flag; // 1 = 수정 X, 2 = 수정 O, 3 = 생성, 4 = 삭제
}