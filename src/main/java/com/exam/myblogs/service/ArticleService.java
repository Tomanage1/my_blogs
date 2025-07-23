package com.exam.myblogs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.myblogs.dto.request.ArticleCreateRequest;
import com.exam.myblogs.dto.response.ArticleListResponse;
import com.exam.myblogs.dto.response.ArticleResponse;
import com.exam.myblogs.entity.Article;

public interface ArticleService extends IService<Article> {
    /**
     * 获取文章列表
     */
    ArticleListResponse getArticleList(Integer page, Integer perPage, String sort, String order);

    /**
     * 创建文章
     */
    ArticleResponse createArticle(ArticleCreateRequest request);

    /**
     * 根据ID获取文章详情
     */
    ArticleResponse getArticleById(Integer id);

    /**
     * 根据标题搜索文章
     */
    ArticleListResponse searchArticlesByTitle(String search, Integer page, Integer perPage);

    /**
     * 更新文章
     */
    ArticleResponse updateArticle(Integer id, ArticleCreateRequest request);

    /**
     * 删除文章
     */
    boolean deleteArticle(Integer userId, Integer articleId);
}
