package com.skhu.cloud.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

// Test Entity

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "version")
public class FolderVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parent;

    private String status;

    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time;

    private String title;

    private String kind;

    private Long size;

    @Column(name = "component_id")
    private Long componentId;
}
