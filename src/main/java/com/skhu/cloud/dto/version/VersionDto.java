package com.skhu.cloud.dto.version;

import com.skhu.cloud.dto.FileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class VersionDto extends FileDto {
    private FileDto fileDto; // @ManyToOne
    private Long versionId; // 해당 파일의 versionId

    public VersionDto(Long versionId) {
        this.versionId = versionId;
    }
}
