package com.skyfree.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.skyfree.common.BaseService;
import com.skyfree.entity.Tag;

@Service
public class TagService extends BaseService<Tag> {

    public List<Tag> getTagsByArticleId(Integer id) {
        String sql = "select g.* from article_tag t, tag g where t.tag_id = g.id and t.article_id = :articleId";
        Map<String, Object> params = new HashMap<>();
        params.put("articleId", id);
        return this.namedJdbcTemplate.query(sql, params, BeanPropertyRowMapper.newInstance(Tag.class));
    }

}
