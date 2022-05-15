package com.skhu.cloud.service;

import com.skhu.cloud.service.Impl.CreateMokServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MokServiceTest {

    @Autowired
    private final CreateMokService createMokService = new CreateMokServiceImpl();

    @Test
    @DisplayName("MokServiceTest")
    public void mokServiceTest() {
        System.out.println("====== FileDto List ======");
        System.out.println(createMokService.returnMokFileDto());

        System.out.println("====== FolderDto List ======");
        System.out.println(createMokService.returnMokFolderDtoList());

        System.out.println("====== FileDiffDto List ======");
        System.out.println(createMokService.returnMokFileDiffDto());

        System.out.println("====== FolderDiffDto List ======");
        System.out.println(createMokService.returnMokFolderDiffDtoList());
    }
}
