package com.skhu.cloud.dto;

import com.skhu.cloud.entity.FolderVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FolderVersionDto {

    private Long parent;

    private Long size;

    private LocalDateTime time;

    private Long componentId;

    private String title;

    private String content;

    private String status;

    private String kind;

    // Folder Version Dto Builder
    public static FolderVersionDto createFolderVersionDto(FolderVersion folderVersion){
        return FolderVersionDto.builder()
                .parent(folderVersion.getParent())
                .size(folderVersion.getSize())
                .time(folderVersion.getTime())
                .componentId(folderVersion.getComponentId())
                .title(folderVersion.getTitle())
                .content(folderVersion.getContent())
                .status(folderVersion.getStatus())
                .kind(folderVersion.getKind())
                .build();
    }
}
