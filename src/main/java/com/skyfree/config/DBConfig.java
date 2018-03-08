package com.skyfree.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DBConfig {
    @Primary
    @Bean(name = "sqlLiteDataSource")
    @Qualifier("sqlLiteDataSource")//spring装配bean唯一标识
    @ConfigurationProperties(prefix="datasource.sqlLite")
    public DataSource mysqlDataSource() throws Exception {
        return DataSourceBuilder.create().build();
    }
    
    // JdbcTemplate
    @Bean(name = "sqlLiteJdbcTemplate")
    public JdbcTemplate getJdbcTemplate(@Qualifier("sqlLiteDataSource") DataSource sqlLiteDataSource) {
        return new JdbcTemplate(sqlLiteDataSource);
    }

    // NamedParameterJdbcTemplate
    @Bean(name = "sqlLiteNamedJdbcTemplate")
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(@Qualifier("sqlLiteJdbcTemplate") JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }
}
