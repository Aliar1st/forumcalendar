package ru.forumcalendar.forumcalendar.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("forum")
public class ActivityController {

    @GetMapping("")
    public String index(Model model) {

        return "forum/index";
    }
}
