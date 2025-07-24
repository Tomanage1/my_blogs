package com.exam.myblogs.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentResponse {
    private Integer id;
    private String content;
    private Integer parentId;  //父类评论id
    private UserSimpleResponse author;
    private LocalDateTime createdAt;

    @Data
    public static class UserSimpleResponse {
        private Integer id;
        private String nickname;
        private String avatar;
    }
}