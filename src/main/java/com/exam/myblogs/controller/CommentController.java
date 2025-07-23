package com.exam.myblogs.controller;


import com.exam.myblogs.dto.request.CommentRequest;
import com.exam.myblogs.dto.response.CommentResponse;
import com.exam.myblogs.dto.response.Result;
import com.exam.myblogs.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/myblog")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 发布评论
     */
    @PostMapping("/comment")
    public Result<CommentResponse> publishComment(@RequestBody CommentRequest request) {
        CommentResponse comment = commentService.publishComment(request);
        return Result.success(comment);
    }

    /**
     * 获取文章的评论列表
     */
    @GetMapping("/article/{article_id}/comments")
    public Result<List<CommentResponse>> getCommentsByArticleId(
            @PathVariable Integer article_id,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer per_page) {

        List<CommentResponse> comments = commentService.getCommentsByArticleId(article_id, page, per_page);
        return Result.success(comments);
    }

    /**
     * 获取评论的回复
     */
    @GetMapping("/comments/{comment_id}/reply")
    public Result<List<CommentResponse>> getRepliesByCommentId(@PathVariable Integer comment_id) {
        List<CommentResponse> replies = commentService.getRepliesByCommentId(comment_id);
        return Result.success(replies);
    }
}
