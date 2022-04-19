package com.skhu.cloud.repository;

import com.skhu.cloud.entity.FolderVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

// Test Repository
@Repository
public interface FolderVersionRepository extends JpaRepository<FolderVersion, Long> {

    // 해당 디렉토리를 root 로 하는 version 들을 모두 불러온다 , 거기서 가장 현재 version 과 비슷한 거
    List<FolderVersion> findByParentOrderByTimeDesc(Long parent);

    Optional<FolderVersion> findByComponentId(Long componentId);

    // 현재 들어온 시각보다 이른 시각을 탐색
    List<FolderVersion> findByTimeBeforeOrderByTimeDesc(LocalDateTime time);

    // 모든 LocalDateTime 을 불러오는 데 내림차순으로 불러옴
    List<FolderVersion> findAllByOrderByTimeDesc();

    List<FolderVersion> findByParentAndTimeBeforeOrderByTimeDesc(Long parent , LocalDateTime time);

    List<FolderVersion> findByComponentIdAndTimeBeforeOrderByTimeDesc(Long componentId , LocalDateTime time);
}