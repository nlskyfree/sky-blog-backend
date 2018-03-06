package com.skyfree.ctrl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/option")
public class OptionCtrl {
    @RequestMapping("/")
    public List getOption() {
        return new ArrayList<>();
    }
}
