package com.skyfree.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyfree.dto.AdminInfo;

@RestController
@RequestMapping("/auth")
public class AuthCtrl {
    @RequestMapping
    public AdminInfo getAdminInfo() {
        AdminInfo adminInfo = new AdminInfo();
        adminInfo.setGravatar("nl_zhuce@163.com");
        return adminInfo;
    }
}
