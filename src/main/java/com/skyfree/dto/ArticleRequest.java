package com.skyfree.dto;

public class ArticleRequest {
    private Integer page;
    private Integer perPage;
    private Integer hot;
    public Integer getPage() {
        return page;
    }
    public void setPage(Integer page) {
        this.page = page;
    }
    public Integer getPerPage() {
        return perPage;
    }
    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }
    public Integer getHot() {
        return hot;
    }
    public void setHot(Integer hot) {
        this.hot = hot;
    }
}
