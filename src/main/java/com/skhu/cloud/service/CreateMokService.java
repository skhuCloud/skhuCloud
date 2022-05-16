package com.skhu.cloud.service;

import com.skhu.cloud.dto.FileDto;
import com.skhu.cloud.dto.diff.FileDiffDto;
import com.skhu.cloud.dto.diff.FolderDiffDto;
import com.skhu.cloud.dto.version.VersionDto;
import com.skhu.cloud.model.Content;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CreateMokService {
    // Folder Version 조회
    List<FolderDiffDto> returnMokFolderDtoList(String name);

    // Folder Version diff 조회
    List<FolderDiffDto> returnMokFolderDiffDtoList();

    // File Version 조회
    FileDiffDto returnMokFileDto();

    // Mok File Version Diff 조회
    List<FileDiffDto> returnMokFileDiffDto();

    // Mok Version Dto List 조회
    List<VersionDto> returnMokVersionDtoList(String name, String kind);

    // Mok File Dto 를 반환
    FileDto createMokFileDto(String name, String path, String kind);

    // Mok File Diff Dto 를 반환
    FileDiffDto createMokFileDiffDto(String name, Long versionId, List<Content> contentList);

    // Mok Folder Diff Dto 를 반환
    FolderDiffDto createMokFolderDiffDto(String name, Long versionId, Long flag);

    // Mok File Diff Dto 의 Content 를 채워줌
    void addContentComponent(Long flag, List<Content> content);

    // Mok Version Dto 를 반환
    VersionDto createMokVersionDto(String name, String path, String kind, Long versionId);
}
