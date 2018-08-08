package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.forumcalendar.forumcalendar.model.form.UserForm;
import ru.forumcalendar.forumcalendar.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String index(
            Model model
    ) {
        model.addAttribute("user", userService.getCurrentUser());
        return "user/index";
    }

    @GetMapping("/edit")
    private String edit(
            Model model
    ) {
        model.addAttribute("user", userService.getCurrentUser());
        return "user/edit";
    }

    @PostMapping("/edit")
    private String edit(
            @Valid UserForm userForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "user/edit";
        }

        userService.save(userForm);

        return "redirect:";
    }
}
