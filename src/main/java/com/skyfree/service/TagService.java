package com.skyfree.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.skyfree.entity.Tag;

@Service
public class TagService {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<Tag> getTags() {
        String sql = "select * from tag";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Tag>());
    }

}
