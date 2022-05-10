package com.skhu.cloud.dto.version;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VersionDto {
    private Long versionId; // Version Id
    private LocalDateTime modifiedTime; // 수정시각
    private Long kind; // 유형, 1 = 폴더, 2 = 파일 (유형을 알아야지, FileDiff 와 FolderDiff 중 어떤 것을 요청할지 정함)
}
