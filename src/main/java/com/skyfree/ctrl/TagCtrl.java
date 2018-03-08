package com.skyfree.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyfree.entity.Tag;
import com.skyfree.service.TagService;

@RestController
@RequestMapping("/tag")
public class TagCtrl {
    @Autowired
    private TagService tagService;
    
    @RequestMapping
    public List<Tag> getTags() {
        return tagService.findAll();
    }
}
