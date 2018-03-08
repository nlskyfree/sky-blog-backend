package com.skyfree.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyfree.dto.ArticleRequest;
import com.skyfree.entity.Article;
import com.skyfree.service.ArticleService;

@RestController
@RequestMapping("/article")
public class ArticleCtrl {
    
    @Autowired
    private ArticleService articleService;
    
    @RequestMapping
    public List<Article> getArticles(ArticleRequest request) {
        return articleService.getArticles(request);
    }
    
    @RequestMapping("/{id}")
    public Article getArticleById(@PathVariable Integer id) {
        return articleService.getArticleById(id);
    }
}
