package com.skyfree.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.skyfree.dto.ArticleRequest;
import com.skyfree.entity.Article;

@Service
public class ArticleService {
    
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<Article> getArticles(ArticleRequest request) {
        String sql = "select * from article";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Article>());
    }
}
