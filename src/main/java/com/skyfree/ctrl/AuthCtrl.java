package com.skyfree.ctrl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthCtrl {
    @RequestMapping("/")
    public List getAuth() {
        return new ArrayList<>();
    }
}
