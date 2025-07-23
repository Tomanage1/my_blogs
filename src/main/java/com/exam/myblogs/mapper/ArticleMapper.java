package com.exam.myblogs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exam.myblogs.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    // 分页查询文章列表
    IPage<Article> selectArticles(IPage<Article> page);

    // 根据标题搜索文章
    //@Param 是 MyBatis 提供的一个注解，用于显式命名方法参数
    /*
    java类中可以使用Param 注解来显式命名方法参数，这样在SQL语句中就可以使用参数名称进行引用。
    List<Article> selectByTitleAndContent(@Param("title") String title, @Param("content") String content);
    xml中使用参数名称进行引用。
     <select id="selectByTitleAndContent" resultType="Article">
        SELECT * FROM article WHERE title LIKE #{title} AND content LIKE #{content}
     </select>
    */
    IPage<Article> searchArticlesByTitle(IPage<Article> page, @Param("title") String title);

    // 根据文章ID查询文章详情（包含标签信息）
    Article selectArticleWithTagsById(Integer id);
}
