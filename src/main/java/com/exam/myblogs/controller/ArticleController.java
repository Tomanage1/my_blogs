package com.exam.myblogs.controller;

import com.exam.myblogs.dto.request.ArticleCreateRequest;
import com.exam.myblogs.dto.response.ArticleListResponse;
import com.exam.myblogs.dto.response.ArticleResponse;
import com.exam.myblogs.dto.response.Result;
import com.exam.myblogs.service.ArticleService;
import com.exam.myblogs.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/myblog")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 获取文章列表
     */
    @GetMapping("/articles")
    public Result<ArticleListResponse> getArticleList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer per_page,
            @RequestParam(defaultValue = "created_at") String sort,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(required = false) String search) {

        // 如果有搜索参数，则执行搜索
        if (search != null && !search.isEmpty()) {
            ArticleListResponse response = articleService.searchArticlesByTitle(search, page, per_page);
            return Result.success(response);
        }

        // 否则获取所有文章列表
        ArticleListResponse response = articleService.getArticleList(page, per_page, sort, order);
        return Result.success(response);
    }

    /**
     * 创建文章
     */
    @PostMapping("/article/creat")
    public Result<ArticleResponse> createArticle(@RequestBody ArticleCreateRequest request) {
        ArticleResponse article = articleService.createArticle(request);
        return Result.success(article);
    }

    /**
     * 根据ID获取文章详情
     */
    @GetMapping("/article/{id}")
    public Result<ArticleResponse> getArticleById(@PathVariable Integer id) {
        ArticleResponse article = articleService.getArticleById(id);
        return Result.success(article);
    }

    /**
     * 更新文章
     */
    @PutMapping("/article")
    public Result<ArticleResponse> updateArticle(
            @RequestParam Integer id,
            @RequestBody ArticleCreateRequest request) {
        ArticleResponse article = articleService.updateArticle(id, request);
        return Result.success(article);
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/article")
    public Result<Boolean> deleteArticle(@RequestParam Integer id) {
        Subject subject = SecurityUtils.getSubject();
        AccountProfile profile = (AccountProfile) subject.getPrincipal();

        boolean result = articleService.deleteArticle(profile.getId(), id);
        return Result.success(result);
    }
}
