package com.exam.myblogs.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleResponse {
    private Integer id;
    private String title;
    private String content;
    private String summary;
    private UserSimpleResponse author;
    private List<String> tags;
    private Integer view;
    private String category;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Meta meta;

    @Data
    public static class Meta {
        private Integer likes;
    }

    @Data
    public static class UserSimpleResponse {
        private Integer id;
        private String username;
        private String avatar;
    }
}