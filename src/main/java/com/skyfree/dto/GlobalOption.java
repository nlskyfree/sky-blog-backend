package com.skyfree.dto;

import com.skyfree.common.PrimaryKey;

public class GlobalOption {
    @PrimaryKey
    private Integer id;
    private Integer likes;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getLikes() {
        return likes;
    }
    public void setLikes(Integer likes) {
        this.likes = likes;
    }
}
