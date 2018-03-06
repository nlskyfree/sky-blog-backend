package com.skyfree.ctrl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyfree.entity.Announcement;

@RestController
@RequestMapping("/announcement")
public class AnnouncementCtrl {
    @RequestMapping("/")
    public List<Announcement> getArticles() {
        List<Announcement> announcements = new ArrayList<>();
        Announcement announcement1 = new Announcement();
        announcement1.setContent("别他妈聊前端了");
        Announcement announcement2 = new Announcement();
        announcement2.setContent("别他妈聊前端了");
        announcements.add(announcement1);
        announcements.add(announcement2);
        return announcements;
    }
}
