package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.forumcalendar.forumcalendar.service.TeamService;
import ru.forumcalendar.forumcalendar.service.UserService;

@Controller
@RequestMapping("team")
public class TeamController {

    private final UserService userService;
    private final TeamService teamService;

    @Autowired
    public TeamController(
            UserService userService,
            TeamService teamService
    ) {
        this.userService = userService;
        this.teamService = teamService;
    }

    @GetMapping("/getLink")
    private String generateLink(Model model) {
        //для теста, т.к. на твоём акке нет команд.
        //model.addAttribute("teamsList", userService.getUserById("1").getUserTeams());
        model.addAttribute("teamsList", userService.getCurrentUser().getUserTeams());
        model.addAttribute("teamRoles", teamService.getAllRoles());
        return "team/genLink";
    }

    @ResponseBody
    @PostMapping("/getLink")
    private String generateLink(
            @RequestParam int teamRoleId,
            @RequestParam int teamId,
            Model model
    ) {
        return teamService.generateLink(teamId, teamRoleId);
    }

    @GetMapping("/invite/{uniqueParam}")
    private String inviteViaLink(@PathVariable("uniqueParam") String uniqueParam) {
        teamService.inviteViaLink(uniqueParam);
        return "redirect:/user/";
    }
}
