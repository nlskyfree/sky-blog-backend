package com.skyfree.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyfree.dto.GlobalOption;

@RestController
@RequestMapping("/option")
public class GlobalOptionCtrl {
    
    @RequestMapping
    public GlobalOption getGlobalOption() {
        GlobalOption globalOption = new GlobalOption();
        globalOption.setId(1);
        globalOption.setLikes(5);
        return globalOption;
    }
}
