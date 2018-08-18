package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.forumcalendar.forumcalendar.service.LikeService;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

//    @GetMapping("/likes")
//    public String likes(Model model) {
//
//        model.addAttribute("likes", likeService.getLikes(5));
//        model.addAttribute("dislikes", likeService.getDislikes(5));
//        return "event/likeTest";
//    }

//    @PostMapping("/like")
//    @ResponseBody
//    public Map<String, Object> like() {
//
//        likeService.like(5);
//
//        Map<String, Object> map = new HashMap<>();
//
//        map.put("status", "ok");
//        map.put("likecount", likeService.getLikes(5));
//
//        return map;
//    }
//
//    @PostMapping("/dislike")
//    @ResponseBody
//    public Map<String, Object> dislike() {
//
//        likeService.dislike(5);
//
//        Map<String, Object> map = new HashMap<>();
//
//        map.put("status", "ok");
//        map.put("dislikecount", likeService.getDislikes(5));
//        return map;
//    }
}
