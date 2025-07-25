package com.exam.myblogs.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Data
public class CommentResponse implements Serializable{
    private Integer id;
    private String content;
    private Integer parentId;  //父类评论id
    private UserSimpleResponse author;
    private LocalDateTime createdAt;

    @Data
    public static class UserSimpleResponse implements Serializable{
        private Integer id;
        private String nickname;
        private String avatar;
    }
}