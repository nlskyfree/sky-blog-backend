package com.skyfree.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyfree.entity.Category;
import com.skyfree.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryCtrl {
    
    @Autowired
    private CategoryService categoryService;
    
    @RequestMapping
    public List<Category> getCategories() {
        return categoryService.findAll();
    }
}
