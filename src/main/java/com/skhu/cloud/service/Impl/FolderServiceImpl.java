package com.skhu.cloud.service.Impl;
//
//import com.skhu.cloud.dto.version.FolderVersionDto;
//import com.skhu.cloud.entity.FolderVersion;
//import com.skhu.cloud.repository.FolderVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Stack;
//import java.util.stream.Collectors;
//
@Service
@RequiredArgsConstructor
public class FolderServiceImpl {
//
//    private final FolderVersionRepository folderVersionRepository;
//    // FolderVersionDto 로 만들어서 반환
//    @Override
//    public List<FolderVersionDto> getFolderVersionDtoList(List<FolderVersion> folderVersionList) {
//        return folderVersionList.stream().map(
//                folderVersion -> FolderVersionDto.createFolderVersionDto(folderVersion))
//                .collect(Collectors.toList());
//    }
//
//    // parent 값을 이용하여서 , DirectoryList 를 반환할 것임
//    @Override
//    public List<String> getDirectoryList(Long parent , LocalDateTime time) {
//        Stack<String> stack = new Stack<>();
//        List<String> result = new ArrayList<>();
//
//        while(true){
//            if(parent == 0) break;
//
//            // 잘못 생각했던 게 , componentId 로 검색해야지 부모를 유니크하게 검색이 가능하려나?
//            FolderVersion folderVersion = getComponent(parent , time);
//            stack.push(folderVersion.getTitle());
//            parent = folderVersion.getParent();
//        }
//
//        while(!stack.isEmpty()){
//            result.add(stack.pop());
//        }
//
//        return result;
//    }
//
//    @Override
//    public FolderVersion getComponent(Long componentId, LocalDateTime time) {
//        return folderVersionRepository.findByComponentIdAndTimeBeforeOrderByTimeDesc(componentId , time).get(0);
//    }
//
//    @Override
//    public List<FolderVersion> getFolderVersionList(Long parent, LocalDateTime time) {
//        HashSet<Long> set = new HashSet<>();
//
//        return folderVersionRepository.findByParentAndTimeBeforeOrderByTimeDesc(parent , time).stream().filter(
//                version -> !set.contains(version.getComponentId())).map(version -> {
//            set.add(version.getComponentId());
//            return version;
//        }).collect(Collectors.toList());
//    }
//
}
