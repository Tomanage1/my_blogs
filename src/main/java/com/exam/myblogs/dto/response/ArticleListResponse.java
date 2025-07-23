package com.exam.myblogs.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class ArticleListResponse {
    private List<ArticleResponse> list;
    private PageInfo meta;

    @Data
    public static class PageInfo {
        private Integer total;
        private Integer page;
        private Integer perPage;
        private Integer totalPages;
        private String sort;
        private String order;
    }
}