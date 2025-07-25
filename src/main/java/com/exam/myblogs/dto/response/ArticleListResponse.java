package com.exam.myblogs.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ArticleListResponse implements Serializable {
    private List<ArticleResponse> list;
    private PageInfo meta;

    @Data
    public static class PageInfo implements Serializable{
        private Integer total;
        private Integer page;
        private Integer perPage;
        private Integer totalPages;
        private String sort;
        private String order;
    }
}