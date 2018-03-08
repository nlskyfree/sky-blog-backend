package com.skyfree.entity;

public class ArticleCategory {
    private Integer id;
    private Integer articleId;
    private Integer categoryId;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getArticleId() {
        return articleId;
    }
    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
    public Integer getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
