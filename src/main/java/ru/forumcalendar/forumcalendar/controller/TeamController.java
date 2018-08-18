package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.forumcalendar.forumcalendar.config.constt.SessionAttributeName;
import ru.forumcalendar.forumcalendar.domain.Team;
import ru.forumcalendar.forumcalendar.domain.User;
import ru.forumcalendar.forumcalendar.service.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping("team")
public class TeamController {

    private static final String HTML_FOLDER = "team/";

    private final PermissionService permissionService;
    private final TeamRoleService teamRoleService;
    private final TeamService teamService;
    private final UserService userService;
    private final LinkService linkService;

    @Autowired
    public TeamController(
            PermissionService permissionService,
            TeamRoleService teamRoleService,
            TeamService teamService,
            UserService userService,
            LinkService linkService
    ) {
        this.permissionService = permissionService;
        this.teamRoleService = teamRoleService;
        this.teamService = teamService;
        this.userService = userService;
        this.linkService = linkService;
    }

    @GetMapping("/teams")
    public String teams(
            @RequestParam int shiftId,
            Authentication authentication,
            Model model
    ) {
        String perm = permissionService.identifyUser(authentication, shiftId, "Shift", "w");

        model.addAttribute(perm, true);
        model.addAttribute("teams", teamService.getTeamModelsByShiftId(shiftId));
        model.addAttribute("curShiftId", shiftId);

        return HTML_FOLDER + "/teams";
    }

    @GetMapping("/{teamId}")
    public String showTeam(
            @PathVariable int teamId,
            Model model,
            Principal principal,
            Authentication authentication
    ) {

        Team team = teamService.get(teamId);
        User user = userService.getCurrentUser(principal);

        if (teamRoleService.isUserCuratorOfTeam(user.getId(), team.getId())) {
            model.addAttribute("isCurator", true);
        } else if (teamRoleService.isUserCaptainOfTeam(user.getId(), team.getId())) {
            model.addAttribute("isCaptain", true);
        } else if (teamRoleService.isUserMemberOfTeam(user.getId(), team.getId())) {
            model.addAttribute("isMember", true);
        }

        String perm = permissionService.identifyUser(authentication, team.getId(), "Team", "w");

        model.addAttribute(perm, true);
        model.addAttribute("teamName", team.getName());
        model.addAttribute("teamUsers", teamService.getTeamMembers(team.getId()));

        return HTML_FOLDER + "/team";
    }

//    @GetMapping("/my")
//    public String teamUsers(
//            Model model,
//            HttpSession httpSession
//    ) {
//
//        Team team = teamService.get((int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE));
//
//        model.addAttribute("teamName", team.getName());
//        model.addAttribute("teamUsers", teamService.getTeamMembers(team.getId()));
//
//        return "team/index";
//    }

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

    @GetMapping("{teamId}/editName")
    public String editName(
            @PathVariable int teamId,
            Model model
    ) {

        Team team = teamService.get(teamId);
        model.addAttribute("teamId", teamId);
        model.addAttribute("teamName", team.getName());

        return HTML_FOLDER + "/_edit_team_name";
    }

    @ResponseBody
    @PostMapping("{teamId}/editName")
    public String editName(
            @PathVariable int teamId,
            String teamName
    ) {

        Team team = teamService.get(teamId);

        if (!teamName.isEmpty()) {
            return teamService.updateTeamName(teamId, teamName).getName();
        }

        return team.getName();
    }

    @GetMapping("/getLink")
    private String generateLink(Model model) {
        model.addAttribute("teamsList", userService.getCurrentUser().getUserTeams());
        model.addAttribute("teamRoles", teamRoleService.getAllRoles());
        return HTML_FOLDER + "/_genLink";
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
