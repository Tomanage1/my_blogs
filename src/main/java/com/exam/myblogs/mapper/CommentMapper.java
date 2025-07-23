package com.exam.myblogs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exam.myblogs.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    // 根据文章ID查询评论列表
    IPage<Comment> selectCommentsByArticleId(IPage<Comment> page, @Param("articleId") Integer articleId);

    // 根据评论ID查询回复
    List<Comment> selectRepliesByCommentId(@Param("commentId") Integer commentId);
}
