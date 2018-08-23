package ru.forumcalendar.forumcalendar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    public static final String REDIRECT_TO_MENU = "redirect:/menu";

    @GetMapping("")
    public String add() {
        return "";
    }
}
