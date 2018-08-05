package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.forumcalendar.forumcalendar.service.UserService;

@Controller
public class HomeController {

    private UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseBody
    public String index() {
        return "123";
    }

    @GetMapping("/home")
    @ResponseBody
    public String home() {
        return userService.getCurrentUser().getEmail() + "  " + userService.getCurrentUser().getAuthorities();
    }
}
