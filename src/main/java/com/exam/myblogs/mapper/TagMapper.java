package com.exam.myblogs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.myblogs.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    // 根据文章ID查询标签
    List<Tag> selectTagsByArticleId(@Param("articleId") Integer articleId);
}
