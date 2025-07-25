package com.exam.myblogs.controller;

import com.exam.myblogs.dto.request.ArticleCreateRequest;
import com.exam.myblogs.dto.response.ArticleListResponse;
import com.exam.myblogs.dto.response.ArticleResponse;
import com.exam.myblogs.dto.response.Result;
import com.exam.myblogs.service.ArticleService;
import com.exam.myblogs.shiro.AccountProfile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "文章Controller", tags = {"文章接口"})
@RestController
@RequestMapping("/myblog")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 获取文章列表
     */
    @ApiOperation(value = "获取文章列表", tags = {"文章接口"})
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
    @ApiOperation(value = "创建文章", tags = {"文章接口"})
    @PostMapping("/article/creat")
    public Result<ArticleResponse> createArticle(@RequestBody ArticleCreateRequest request) {
        ArticleResponse article = articleService.createArticle(request);
        return Result.success(article);
    }

    /**
     * 根据ID获取文章详情
     */
    @ApiOperation(value = "获取文章详情", tags = {"文章接口"})
    @GetMapping("/article/{id}")
    public Result<ArticleResponse> getArticleById(@PathVariable Integer id) {
        ArticleResponse article = articleService.getArticleById(id);
        return Result.success(article);
    }

    /**
     * 更新文章
     */
    @ApiOperation(value = "更新文章", tags = {"文章接口"})
    @PutMapping("/article")
    public Result<ArticleResponse> updateArticle(
            @RequestParam Integer id,//文章id
            @RequestBody ArticleCreateRequest request) {
        ArticleResponse article = articleService.updateArticle(id, request);
        return Result.success(article);
    }

    /**
     * 删除文章
     */
    @ApiOperation(value = "删除文章", tags = {"文章接口"})
    @DeleteMapping("/article")
    public Result<Boolean> deleteArticle(@RequestParam Integer id) {
        Subject subject = SecurityUtils.getSubject();
        AccountProfile profile = (AccountProfile) subject.getPrincipal();

        boolean result = articleService.deleteArticle(profile.getId(), id);
        return Result.success(result);
    }
}
