package com.skhu.cloud.service.Impl;

import com.skhu.cloud.dto.FileDto;
import com.skhu.cloud.dto.diff.FileDiffDto;
import com.skhu.cloud.dto.diff.FolderDiffDto;
import com.skhu.cloud.dto.version.VersionDto;
import com.skhu.cloud.model.Content;
import com.skhu.cloud.service.CreateMokService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CreateMokServiceImpl implements CreateMokService {
    static String FOLDER_IMAGE_URL = "https://res-1.cdn.office.net/files/fabric-cdn-prod_20220127.003/assets/item-types/20/sharedfolder.svg";
    static String FILE_IMAGE_URL = "https://res-1.cdn.office.net/files/fabric-cdn-prod_20220127.003/assets/item-types/20/genericfile.svg";
    static Random random = new Random(); // random 으로 flag 값을 넣어주기 위함

    @Override
    public List<FolderDiffDto> returnMokFolderDtoList() {
        List<FolderDiffDto> mokFolderDtoList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            mokFolderDtoList.add(createMokFolderDiffDto((long) random.nextInt(5), 1L)); // versionId 는 랜덤으로 flag 값을 1 로 넘겨줌으로서 그냥 Folder Version 조회
        }

        return mokFolderDtoList;
    }

    @Override
    public List<FolderDiffDto> returnMokFolderDiffDtoList() {
        List<FolderDiffDto> mokFolderDiffDtoList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            mokFolderDiffDtoList.add(createMokFolderDiffDto((long) random.nextInt(5), (long) (random.nextInt(4) + 1))); // flag 1 ~ 4 까지 보내준다.
        }

        return mokFolderDiffDtoList;
    }

    @Override
    public FileDiffDto returnMokFileDto() {
        List<Content> contentList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            addContentComponent(1L, contentList);
        } // 10 줄 짜리의 mokDate 만들음

        return createMokFileDiffDto((long) random.nextInt(5), contentList); // Version Id 무작위로 배정
    }

    @Override
    public List<FileDiffDto> returnMokFileDiffDto() {
        List<Content> contentListVersion1 = new ArrayList<>();
        List<Content> contentListVersion2 = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            addContentComponent((long) (random.nextInt(3) + 1), contentListVersion1); // flag 값을 1 ~ 3 까지 넘겨줌
            addContentComponent((long) (random.nextInt(3) + 1), contentListVersion2);
        }

        List<FileDiffDto> fileDiffDtoList = new ArrayList<>();
        fileDiffDtoList.add(createMokFileDiffDto(1L, contentListVersion1));
        fileDiffDtoList.add(createMokFileDiffDto(2L, contentListVersion2));

        return fileDiffDtoList;
    }


    @Override
    public FileDto createMokFileDto(String path, String kind) {
        return FileDto.builder()
                .name("hello")
                .modifiedTime(FileDto.modifiedTime(System.currentTimeMillis()))
                .kind(kind)
                .path(path)
                .size("100")
                .imageUrl((kind.equals("폴더")) ? FOLDER_IMAGE_URL : FILE_IMAGE_URL)
                .build();
    }

    @Override
    public FileDiffDto createMokFileDiffDto(Long versionId, List<Content> contentList) {
        return new FileDiffDto(createMokVersionDto("Users/file", "파일", versionId), contentList);
    }

    @Override
    public FolderDiffDto createMokFolderDiffDto(Long versionId, Long flag) { // Mok Folder Dto 를 받은 flag 값으로 반환
        return new FolderDiffDto(createMokVersionDto("Users/folder", "폴더", versionId), flag);
    }

    @Override
    public void addContentComponent(Long flag, List<Content> contentList) { // flag 와 contentList 를 넘기면, 추가해줌
        String appendString = "안녕하세요 " + (contentList.size() + random.nextInt(10)) + " 번째 요소입니다.\n";

        contentList.add(new Content(flag, appendString)); // 요소를 추가
    }

    @Override
    public VersionDto createMokVersionDto(String path, String kind, Long versionId) { // Mok Version Dto 를 받은 VersionId 값을 매겨서 반환
        return new VersionDto(createMokFileDto(path, kind), versionId); // folder or file version dto 를 반환함
    }
}
