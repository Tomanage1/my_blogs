package com.exam.myblogs.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.time.LocalDateTime;


@ApiModel(value = "评论请求参数")
@Data
public class CommentRequest {
    private Integer userId;
    private String content;
    private Integer articleId;
    private LocalDateTime createdAt;
    private Integer parentId; // 可选，用于回复评论
}