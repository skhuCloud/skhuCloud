package com.skhu.cloud.dto;

import com.skhu.cloud.dto.diff.FileDiffDto;
import com.skhu.cloud.dto.version.VersionDto;
import com.skhu.cloud.model.Content;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

@SpringBootTest
public class VersionDtoTest {

    @Test
    @DisplayName("FileDiffDtoTest")
    public void fileDiffDtoTest() {
//        List<Content> fileDiffDtoList = new ArrayList<>();
//
//        Random random = new Random();
//        for (int i = 0; i < 10; i++) {
//            fileDiffDtoList.add(new Content((long) random.nextInt(2), i + ""));
//        }
//
//        FileDiffDto fileDiffDto = new FileDiffDto(VersionDto.builder().FileDto.builder()
//                .name("hello")
//                .build(), fileDiffDtoList);
//
//        System.out.println(fileDiffDto);
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
