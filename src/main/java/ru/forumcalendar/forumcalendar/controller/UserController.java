package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    private static final String HTML_FOLDER = "user/";
    private static final String REDIRECT_ROOT_MAPPING = "redirect:/user/";

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

    @GetMapping("{id}")
    public String index(
            @PathVariable String id,
            Model model
    ) {
        model.addAttribute("isMyPage", id.equals(userService.getCurrentId()));
        model.addAttribute("user", conversionService.convert(userService.get(id), UserModel.class));

        return HTML_FOLDER + "/index";
    }

    @GetMapping("{id}/edit")
    public String edit(
            @PathVariable String id,
            Model model
    ) {
        if (!id.equals(userService.getCurrentId())) {
            return REDIRECT_ROOT_MAPPING + id;
        }

        model.addAttribute(new UserForm(userService.getCurrentUser()));
        model.addAttribute("contactTypes", contactTypeService.getAll());

        return "user/edit";
    }

    @PostMapping("{id}/edit")
    public String edit(
            @PathVariable String id,
            @Valid UserForm userForm,
            BindingResult bindingResult
    ) {
        if (!id.equals(userService.getCurrentId())) {
            return REDIRECT_ROOT_MAPPING + id;
        }

        if (bindingResult.hasErrors()) {
            return "user/edit";
        }

        userService.save(userForm);

        return "redirect:";
    }
}
