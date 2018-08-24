package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @ResponseBody
    @PostMapping("/like")
    public Map<String, Object> like(
            @RequestParam int eventId
    ) {
        likeService.like(eventId);

        Map<String, Object> map = new HashMap<>();

        map.put("status", "ok");
        map.put("dislikecount", likeService.getDislikes(eventId));
        map.put("likecount", likeService.getLikes(eventId));
        return map;
    }

    @ResponseBody
    @PostMapping("/dislike")
    public Map<String, Object> dislike(
            @RequestParam int eventId
    ) {

        likeService.dislike(eventId);

        Map<String, Object> map = new HashMap<>();

        map.put("status", "ok");
        map.put("dislikecount", likeService.getDislikes(eventId));
        map.put("likecount", likeService.getLikes(eventId));
        return map;
    }
}
