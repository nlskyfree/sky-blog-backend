package com.skyfree.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyfree.entity.Github;
import com.skyfree.service.GithubService;

@RestController
@RequestMapping("/github")
public class GithubCtrl {
    @Autowired  
    private GithubService githubService;
    
    @RequestMapping
    public List<Github> getGithubs() {
        return githubService.findAll();
    }
}
