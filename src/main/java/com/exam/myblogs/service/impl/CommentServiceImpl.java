package com.exam.myblogs.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exam.myblogs.dto.request.CommentRequest;
import com.exam.myblogs.dto.response.CommentResponse;
import com.exam.myblogs.dto.response.UserResponse;
import com.exam.myblogs.entity.Comment;
import com.exam.myblogs.mapper.CommentMapper;
import com.exam.myblogs.service.CommentService;
import com.exam.myblogs.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    // 发布评论
    @Override
    public CommentResponse publishComment(CommentRequest request) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(request, comment);
        comment.setId(request.getUserId());
        // 如果是回复评论，设置parentId
        if (request.getParentId() != null) {
            comment.setParentId(request.getParentId());
        } else {
            comment.setParentId(0); // 0表示是顶级评论
        }

        baseMapper.insert(comment);

        return convertToResponse(comment);
    }

    // 获取文章的评论
    @Override
    public List<CommentResponse> getCommentsByArticleId(Integer articleId, Integer page, Integer perPage) {
        IPage<Comment> commentPage = new Page<>(page, perPage);
        IPage<Comment> resultPage = baseMapper.selectCommentsByArticleId(commentPage, articleId);

        return resultPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 获取评论的回复
    @Override
    public List<CommentResponse> getRepliesByCommentId(Integer commentId) {
        List<Comment> replies = baseMapper.selectRepliesByCommentId(commentId);

        return replies.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 转换实体类到响应DTO
     */
    private CommentResponse convertToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        BeanUtils.copyProperties(comment, response);

        // 设置评论者信息
        CommentResponse.UserSimpleResponse author = new CommentResponse.UserSimpleResponse();
        UserResponse user = userService.getUserById(comment.getAuthorId());
        if (user != null) {
            author.setId(user.getId());
            author.setNickname(user.getNickname());
            author.setAvatar(user.getAvatar());
        }
        response.setAuthor(author);

        return response;
    }
}
