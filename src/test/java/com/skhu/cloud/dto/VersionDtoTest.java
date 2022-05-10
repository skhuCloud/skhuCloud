package com.skhu.cloud.dto;

import com.skhu.cloud.dto.diff.FileDiffDto;
import com.skhu.cloud.dto.version.VersionDto;
import com.skhu.cloud.model.Content;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.ArrayList;

@SpringBootTest
public class VersionDtoTest {

    @Test
    @DisplayName("FileDiffDtoTest")
    public void fileDiffDtoTest() {
        // 자식 클래스 속성은 무조건 다 채워야 함, 근데 채워야 하는 것들만 넣어놓긴했음
        List<Content> fileDiffDtoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            fileDiffDtoList.add(new Content((long) i, i + ""));
        }

        FileDiffDto fileDiffDto = new FileDiffDto(FileDto.builder()
                .name("hello")
                .build(), fileDiffDtoList);

        System.out.println(fileDiffDto);
    }

    @Test
    @DisplayName("FolderDiffDtoTest")
    public void folderDiffDtoTest() {

    }

    @Test
    @DisplayName("VersionDtoTest")
    public void versionDtoTest() {
        VersionDto versionDto = new VersionDto(FileDto.builder()
                .name("hello")
                .build(), 1L);

        System.out.println(versionDto);
    }
}
