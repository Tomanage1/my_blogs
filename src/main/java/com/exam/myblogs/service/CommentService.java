package com.exam.myblogs.service;

import com.exam.myblogs.dto.request.CommentRequest;
import com.exam.myblogs.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {
    /**
     * 发布评论
     */
    CommentResponse publishComment(CommentRequest request);

    /**
     * 根据文章ID获取评论列表
     */
    List<CommentResponse> getCommentsByArticleId(Integer articleId, Integer page, Integer perPage);

    /**
     * 获取评论的回复
     */
    List<CommentResponse> getRepliesByCommentId(Integer commentId);
}
