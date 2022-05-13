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
    List<FolderDiffDto> createMokFolderDtoList();

    // Folder Version diff 조회
    List<FolderDiffDto> createMokFolderDiffDtoList();

    // File Version 조회
    FileDiffDto createMokFileDto();

    // File Version Diff 조회
    FileDiffDto createMokFileDiffDto();

    // path 와 kind 로 Folder, File Version Dto 를 조회
    List<VersionDto> createMokVersionDtoList(String path, String kind);

    // MokFileDto 를 반환
    FileDto createMokFileDto(String path, String kind);

    // Mok Folder Dto 를 반환
    FolderDiffDto createMokFolderDto(Long flag);

    // Mok File Dto 를 반환
    void addContentComponent(Long flag, List<Content> content);

    // Mok Version Dto 를 반환
    VersionDto createMokVersionDto(String path, String kind, Long versionId);
}
