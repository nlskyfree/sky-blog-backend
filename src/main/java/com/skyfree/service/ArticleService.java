package com.skyfree.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skyfree.common.BaseService;
import com.skyfree.dto.ArticleRequest;
import com.skyfree.entity.Article;
import com.skyfree.entity.Category;
import com.skyfree.entity.Tag;

@Service
public class ArticleService extends BaseService<Article> {
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private TagService tagService;

    public List<Article> getArticles(ArticleRequest request) {
        List<Article> articles = this.findAll();
        for(Article article : articles) {
            Integer id = article.getId();
            List<Category> categories = categoryService.getCategoriesByArticleId(id);
            List<Tag> tags = tagService.getTagsByArticleId(id);
            article.setCategories(categories);
            article.setTags(tags);
        }
        
        return articles;
    }

    public Article getArticleById(Integer id) {
        Article article = this.findById(id);
        List<Category> categories = categoryService.getCategoriesByArticleId(id);
        List<Tag> tags = tagService.getTagsByArticleId(id);
        article.setCategories(categories);
        article.setTags(tags);
        return article;
    }
}
