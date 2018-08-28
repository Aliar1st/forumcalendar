package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.forumcalendar.forumcalendar.service.PermissionService;
import ru.forumcalendar.forumcalendar.service.UserService;

import java.security.Principal;

@Controller
public class AdminMessageController {

    private static final String HTML_FOLDER = "admin/";

    private final UserService userService;

    @Autowired
    public AdminMessageController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    @GetMapping("/admin/sendMessage")
    public String index() {
        return HTML_FOLDER + "index";
    }


    @MessageMapping("/sendAdminMess")
    @SendTo("/receiveAdminMess")
    public String sendMessage(
            String message,
            Principal principal
    ) {
        if (userService.getCurrentUser(principal) == null) {
            return "redirect:/";
        }
        return message;
    }

}
