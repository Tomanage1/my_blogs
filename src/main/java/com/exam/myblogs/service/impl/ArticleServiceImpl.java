package com.exam.myblogs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exam.myblogs.dto.request.ArticleCreateRequest;
import com.exam.myblogs.dto.response.ArticleListResponse;
import com.exam.myblogs.dto.response.ArticleResponse;
import com.exam.myblogs.dto.response.UserResponse;
import com.exam.myblogs.entity.Article;
import com.exam.myblogs.entity.ArticleTag;
import com.exam.myblogs.entity.Tag;
import com.exam.myblogs.mapper.ArticleMapper;
import com.exam.myblogs.mapper.ArticleTagMapper;
import com.exam.myblogs.mapper.TagMapper;
import com.exam.myblogs.service.ArticleService;
import com.exam.myblogs.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private UserService userService;

    // 获取文章列表
    @Override
    public ArticleListResponse getArticleList(Integer page, Integer perPage, String sort, String order) {
        // 创建分页对象，指定当前页码和每页记录数
        IPage<Article> articlePage = new Page<>(page, perPage);
        // 调用 Mapper 层方法进行分页查询，获取分页结果
        IPage<Article> resultPage = baseMapper.selectArticles(articlePage);

        // 创建响应对象
        ArticleListResponse response = new ArticleListResponse();
        // 设置响应对象的分页信息
        ArticleListResponse.PageInfo pageInfo = new ArticleListResponse.PageInfo();
        pageInfo.setTotal((int) resultPage.getTotal());
        pageInfo.setPage(page);
        pageInfo.setPerPage(perPage);
        pageInfo.setTotalPages((int) resultPage.getPages());
        pageInfo.setSort(sort);
        pageInfo.setOrder(order);
        // 设置响应对象的文章列表
        List<ArticleResponse> articleResponses = resultPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        // 完善响应对象
        response.setList(articleResponses);
        response.setMeta(pageInfo);

        return response;
    }

    // 创建文章
    @Transactional
    @Override
    public ArticleResponse createArticle(ArticleCreateRequest request) {
        // 创建文章
        Article article = new Article();
        BeanUtils.copyProperties(request, article);
        article.setAuthorId(request.getAuthorId());

        baseMapper.insertArticleWithCategory(article);

        // 处理标签
        handleArticleTags(article.getId(), request.getTags());

        return getArticleById(article.getId());
    }

    // 获取文章详情
    @Override
    public ArticleResponse getArticleById(Integer id) {
        Article article = baseMapper.selectArticleWithTagsById(id);

        if (article == null) {
            return null;
        }

        return convertToResponse(article);
    }

    /**
     * 根据文章标题搜索文章，并返回分页结果
     *
     * @param search 搜索关键词，用于匹配文章标题
     * @param page 当前页码
     * @param perPage 每页显示的文章数量
     * @return 返回包含文章列表和分页信息的响应对象
     */
    @Override
    public ArticleListResponse searchArticlesByTitle(String search, Integer page, Integer perPage) {
        IPage<Article> articlePage = new Page<>(page, perPage);
        IPage<Article> resultPage = baseMapper.searchArticlesByTitle(articlePage, search);

        ArticleListResponse response = new ArticleListResponse();
        ArticleListResponse.PageInfo pageInfo = new ArticleListResponse.PageInfo();
        pageInfo.setTotal((int) resultPage.getTotal());
        pageInfo.setPage(page);
        pageInfo.setPerPage(perPage);
        pageInfo.setTotalPages((int) resultPage.getPages());

        List<ArticleResponse> articleResponses = resultPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        response.setList(articleResponses);
        response.setMeta(pageInfo);

        return response;
    }

    // 更新文章
    @Transactional
    @Override
    public ArticleResponse updateArticle(Integer id, ArticleCreateRequest request) {
        Article article = baseMapper.selectById(id);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }

        // 检查权限，只有作者可以更新文章
        if (!article.getAuthorId().equals(request.getAuthorId())) {
            throw new RuntimeException("没有权限更新此文章");
        }

        // 更新文章信息
        BeanUtils.copyProperties(request, article);
        article.setId(id);

        baseMapper.updateById(article);

        // 更新标签
        // 先删除原有标签关联
        LambdaQueryWrapper<ArticleTag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ArticleTag::getArticleId, id);
        articleTagMapper.delete(lambdaQueryWrapper);

        // 添加新标签
        handleArticleTags(id, request.getTags());

        return getArticleById(id);
    }

    @Override
    public boolean deleteArticle(Integer userId, Integer articleId) {
        Article article = baseMapper.selectById(articleId);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }

        // 检查权限，只有作者可以删除文章
        if (!article.getAuthorId().equals(userId)) {
            throw new RuntimeException("没有权限删除此文章");
        }

        // 删除文章
        baseMapper.deleteById(articleId);

        // 删除标签关联
        LambdaQueryWrapper<ArticleTag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ArticleTag::getArticleId, articleId);
        articleTagMapper.delete(lambdaQueryWrapper);

        return true;
    }

    /**
     * 处理文章标签
     */
    private void handleArticleTags(Integer articleId, List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return;
        }

        for (String tagName : tagNames) {
            // 查找标签是否已存在
            LambdaQueryWrapper<Tag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Tag::getName, tagName);
            Tag tag = tagMapper.selectOne(lambdaQueryWrapper);

            // 如果标签不存在则创建
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                tagMapper.insert(tag);
            }

            // 建立文章和标签的关联
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(articleId);
            articleTag.setTagId(tag.getId());
            articleTagMapper.insert(articleTag);
        }
    }

    /**
     * 转换实体类到响应DTO
     */
    private ArticleResponse convertToResponse(Article article) {
        ArticleResponse response = new ArticleResponse();
        BeanUtils.copyProperties(article, response);

        //完成不同类中meta的类型转换，并且手动赋值
        ArticleResponse.Meta meta = new ArticleResponse.Meta();
        BeanUtils.copyProperties(article.getMeta(), meta);
        response.setView(article.getView());
        response.setMeta(meta);

        // 设置作者信息
        ArticleResponse.UserSimpleResponse author = new ArticleResponse.UserSimpleResponse();
        UserResponse user = userService.getUserById(article.getAuthorId());
        if (user != null) {
            author.setId(user.getId());
            author.setUsername(user.getUsername());
            author.setAvatar(user.getAvatar());
        }
        response.setAuthor(author);

        // 设置标签
        List<Tag> tags = tagMapper.selectTagsByArticleId(article.getId());
        if (tags != null) {
            List<String> tagNames = tags.stream()
                    .map(Tag::getName)
                    .collect(Collectors.toList());
            response.setTags(tagNames);
        }



        // 设置状态文本
        response.setStatus(article.getStatus() == 1 ? "published" : "draft");

        return response;
    }
}
