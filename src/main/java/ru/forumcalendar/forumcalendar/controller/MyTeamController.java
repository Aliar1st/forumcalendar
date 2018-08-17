package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.forumcalendar.forumcalendar.config.constt.SessionAttributeName;
import ru.forumcalendar.forumcalendar.domain.Team;
import ru.forumcalendar.forumcalendar.domain.User;
import ru.forumcalendar.forumcalendar.service.LinkService;
import ru.forumcalendar.forumcalendar.service.TeamRoleService;
import ru.forumcalendar.forumcalendar.service.TeamService;
import ru.forumcalendar.forumcalendar.service.UserService;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping("team")
public class MyTeamController {

    private final TeamRoleService teamRoleService;
    private final TeamService teamService;
    private final UserService userService;
    private final LinkService linkService;

    @Autowired
    public MyTeamController(
            TeamRoleService teamRoleService,
            TeamService teamService,
            UserService userService,
            LinkService linkService
    ) {
        this.teamRoleService = teamRoleService;
        this.teamService = teamService;
        this.userService = userService;
        this.linkService = linkService;
    }

    @GetMapping("/my")
    public String teamUsers(
            Model model,
            HttpSession httpSession
    ) {

        Team team = teamService.get((int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE));

        model.addAttribute("teamName", team.getName());
        model.addAttribute("teamUsers", teamService.getTeamMembers(team.getId()));

        return "team/index";
    }

    @ResponseBody
    @PostMapping("/becomeCaptain")
    public String becomeCaptain(
            Principal principal,
            HttpSession httpSession
    ) {

        User currentUser = userService.getCurrentUser(principal);
        Team team = teamService.get((int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE));

        if (teamService.becomeCaptainToggle(currentUser.getId(), team.getId())) {
            return "Вы стали капитаном";
        } else {
            return "Вы не капитан";
        }
    }

    @GetMapping("/editName")
    public String editName(
            Model model,
            HttpSession httpSession
    ) {

        Team team = teamService.get((int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE));
        model.addAttribute("teamName", team.getName());

        return "team/_edit_team_name";
    }

    @ResponseBody
    @PostMapping("/editName")
    public String editName(
            String teamName,
            HttpSession httpSession
    ) {

        Team team = teamService.get((int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE));

        if (!teamName.isEmpty()) {
            return teamService.updateTeamName(team.getId(), teamName).getName();
        }

        return team.getName();
    }


    @GetMapping("/getLink")
    private String generateLink(Model model) {
        //для теста, т.к. на твоём акке нет команд.
        //model.addAttribute("teamsList", userService.get("1").getUserTeams());
        model.addAttribute("teamsList", userService.getCurrentUser().getUserTeams());
        model.addAttribute("teamRoles", teamRoleService.getAllRoles());
        return "team/_genLink";
    }

    @ResponseBody
    @PostMapping("/getLink")
    private String generateLink(
            @RequestParam int teamRoleId,
            @RequestParam int teamId
    ) {
        return linkService.generateLink(teamId, teamRoleId);
    }

    @GetMapping("/invite/{uniqueParam}")
    private String inviteViaLink(@PathVariable String uniqueParam) {
        linkService.inviteViaLink(uniqueParam);
        return "redirect:/user/";
    }
}
