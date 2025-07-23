package com.exam.myblogs.dto.request;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleCreateRequest {
    private Integer authorId;
    private String title;
    private String content;
    private String summary;
    private List<String> tags;
    private String category;
    private Integer status; // 0-草稿，1-已发布
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}