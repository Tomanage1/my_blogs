package com.exam.myblogs.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentResponse {
    private Integer id;
    private String content;
    private UserSimpleResponse author;
    private LocalDateTime createdAt;
    private List<CommentResponse> replies; // 用于嵌套回复

    @Data
    public static class UserSimpleResponse {
        private Integer id;
        private String nickname;
        private String avatar;
    }
}