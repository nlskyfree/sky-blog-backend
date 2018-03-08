package com.skyfree.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.skyfree.common.BaseService;
import com.skyfree.entity.Category;

@Service
public class CategoryService extends BaseService<Category> {

    public List<Category> getCategoriesByArticleId(Integer id) {
        String sql = "select g.* from article_category t, category g where t.category_id = g.id and t.article_id = :articleId";
        Map<String, Object> params = new HashMap<>();
        params.put("articleId", id);
        return this.namedJdbcTemplate.query(sql, params, BeanPropertyRowMapper.newInstance(Category.class));
    }

}
