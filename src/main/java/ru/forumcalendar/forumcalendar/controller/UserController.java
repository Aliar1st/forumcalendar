package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.forumcalendar.forumcalendar.model.UserModel;
import ru.forumcalendar.forumcalendar.model.form.UserForm;
import ru.forumcalendar.forumcalendar.service.ContactTypeService;
import ru.forumcalendar.forumcalendar.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final ContactTypeService contactTypeService;
    private final ConversionService conversionService;

    @Autowired
    public UserController(
            UserService userService,
            ContactTypeService contactTypeService,
            @Qualifier("mvcConversionService") ConversionService conversionService
    ) {
        this.userService = userService;
        this.contactTypeService = contactTypeService;
        this.conversionService = conversionService;
    }

    @GetMapping("")
    public String index(
            Model model
    ) {
        model.addAttribute("user", conversionService.convert(userService.getCurrentUser(), UserModel.class));

        return "user/index";
    }

    @GetMapping("/edit")
    public String edit(
            Model model
    ) {
        model.addAttribute(new UserForm(userService.getCurrentUser()));
        model.addAttribute("contactTypes", contactTypeService.getAll());

        return "user/edit";
    }

    @PostMapping("/edit")
    public String edit(
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
