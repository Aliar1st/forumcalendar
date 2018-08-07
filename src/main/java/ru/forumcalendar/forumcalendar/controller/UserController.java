package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.forumcalendar.forumcalendar.model.form.UserForm;
import ru.forumcalendar.forumcalendar.service.TeamService;
import ru.forumcalendar.forumcalendar.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final TeamService teamService;

    @Autowired
    public UserController(
            UserService userService,
            TeamService teamService) {
        this.userService = userService;
        this.teamService = teamService;
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

    @GetMapping("/getLink")
    private String generateLink(Model model) {
        //для теста, т.к. на твоём акке нет команд.
        //model.addAttribute("teamsList", userService.getUserById("1").getUserTeams());
        model.addAttribute("teamsList", userService.getCurrentUser().getUserTeams());
        model.addAttribute("teamRoles", teamService.getAllRoles());
        return "user/genLink";
    }

    @ResponseBody
    @PostMapping("/getLink")
    private String generateLink(
            @RequestParam int teamRoleId,
            @RequestParam int teamId,
            Model model
    ) {
        return userService.generateLink(teamId, teamRoleId);
    }

    @GetMapping("/invite/{uniqueParam}")
    private String inviteViaLink(@PathVariable("uniqueParam") String uniqueParam) {
        userService.inviteViaLink(uniqueParam);
        return "redirect:/user/";
    }

}
