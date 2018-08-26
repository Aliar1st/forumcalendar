package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.forumcalendar.forumcalendar.config.constt.SessionAttributeName;
import ru.forumcalendar.forumcalendar.domain.Team;
import ru.forumcalendar.forumcalendar.domain.User;
import ru.forumcalendar.forumcalendar.model.form.TeamForm;
import ru.forumcalendar.forumcalendar.service.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("team")
public class TeamController {

    private static final String HTML_FOLDER = "team/";
    private static final String REDIRECT_ROOT_MAPPING = "redirect:/team/";

    private final ConversionService conversionService;
    private final ShiftService shiftService;
    private final PermissionService permissionService;
    private final TeamRoleService teamRoleService;
    private final TeamService teamService;
    private final UserService userService;
    private final LinkService linkService;

    private final PermissionEvaluator permissionEvaluator;

    @Autowired
    public TeamController(
            ConversionService conversionService,
            ShiftService shiftService,
            PermissionService permissionService,
            TeamRoleService teamRoleService,
            TeamService teamService,
            UserService userService,
            LinkService linkService,
            PermissionEvaluator permissionEvaluator) {
        this.conversionService = conversionService;
        this.shiftService = shiftService;
        this.permissionService = permissionService;
        this.teamRoleService = teamRoleService;
        this.teamService = teamService;
        this.userService = userService;
        this.linkService = linkService;
        this.permissionEvaluator = permissionEvaluator;
    }

    //@PreAuthorize("hasRole('ROLE_SUPERUSER')")
    @GetMapping("/allTeams")
    public String allTeams(
            @RequestParam int activityId,
            HttpSession httpSession,
            Authentication authentication,
            Model model
    ) {

        int teamId = (int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);
        if (!permissionEvaluator.hasPermission(authentication, teamId, "Team", "w")) {
            return REDIRECT_ROOT_MAPPING + teamId;
        }

        PermissionService.Identifier identifier = PermissionService.Identifier.USER;

        model.addAttribute(identifier.getValue(), true);
        model.addAttribute("teams", teamService.getTeamModelsByActivityId(activityId));
        model.addAttribute("search", false);

        return HTML_FOLDER + "/teams";
    }

    @GetMapping("/teams")
    public String teams(
            @RequestParam int shiftId,
            Model model
    ) {

        PermissionService.Identifier identifier =
                permissionService.identifyUser(shiftService.get(shiftId).getActivity().getId());

        model.addAttribute(identifier.getValue(), true);
        model.addAttribute("teams", teamService.getTeamModelsByShiftId(shiftId));
        model.addAttribute("curShiftId", shiftId);
        model.addAttribute("search", true);

        return HTML_FOLDER + "/teams";
    }

    @GetMapping("/{teamId}")
    public String showTeam(
            @PathVariable int teamId,
            Model model,
            Principal principal
    ) {

        Team team = teamService.get(teamId);
        User user = userService.getCurrentUser(principal);

        model.addAttribute(teamRoleService.getMemberRole(user.getId(), team.getId()).getValue(), true);

        PermissionService.Identifier identifier =
                permissionService.identifyUser(team.getShift().getActivity().getId());

        model.addAttribute(identifier.getValue(), true);
        model.addAttribute("teamId", teamId);
        model.addAttribute("curUserId", user.getId());
        model.addAttribute("teamName", team.getName());
        model.addAttribute("teamUsers", teamService.getTeamMembers(team.getId()));

        return HTML_FOLDER + "/team";
    }

    @ResponseBody
    @PostMapping("/becomeCaptain")
    public String becomeCaptain(
            Principal principal,
            HttpSession httpSession,
            RedirectAttributes redirectAttributes
    ) {

        User currentUser = userService.getCurrentUser(principal);
        int teamId = (int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);

        TeamMemberStatus teamMemberStatus = teamService.getStatus(teamId);
        if (TeamMemberStatus.OK != teamMemberStatus) {
            return teamService.resolveTeamError(teamMemberStatus, httpSession, redirectAttributes);
        }

        Team team = teamService.get(teamId);

        if (teamService.becomeCaptainToggle(currentUser.getId(), teamId)) {
            return "Вы стали капитаном";
        } else {
            return "Вы не капитан";
        }
    }

    @GetMapping("add")
    public String add(
            @RequestParam int shiftId,
            HttpSession httpSession,
            Authentication authentication,
            Model model
    ) {

        int teamId = (int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);
        if (!permissionEvaluator.hasPermission(authentication, teamId, "Team", "w")) {
            return REDIRECT_ROOT_MAPPING + teamId;
        }

        TeamForm teamForm = new TeamForm();
        teamForm.setShiftId(shiftId);

        model.addAttribute(teamForm);

        return HTML_FOLDER + "_add";
    }

    @PostMapping("add")
    public String add(
            @Valid TeamForm teamForm,
            BindingResult bindingResult,
            HttpSession httpSession,
            Authentication authentication
    ) throws IOException {
        int teamId = (int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);
        if (!permissionEvaluator.hasPermission(authentication, teamId, "Team", "w")) {
            return REDIRECT_ROOT_MAPPING + teamId;
        }

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "_add";
        }

        teamService.save(teamForm);

        return "layouts/_empty";
    }

    @GetMapping("{teamId}/editName")
    public String editName(
            @PathVariable int teamId,
            Principal principal,
            Authentication authentication,
            Model model
    ) {
        if (!permissionEvaluator.hasPermission(authentication, teamId, "Team", "w") &&
                !teamRoleService.isUserCuratorOfTeam(userService.getCurrentUser(principal).getId(), teamId)) {
            return null;
        }

        Team team = teamService.get(teamId);
        model.addAttribute("teamId", teamId);
        model.addAttribute("teamName", team.getName());

        return HTML_FOLDER + "/_edit_team_name";
    }

    @ResponseBody
    @PostMapping("{teamId}/editName")
    public String editName(
            @PathVariable int teamId,
            Principal principal,
            Authentication authentication,
            String teamName
    ) {

        if (!permissionEvaluator.hasPermission(authentication, teamId, "Team", "w") &&
                !teamRoleService.isUserCuratorOfTeam(userService.getCurrentUser(principal).getId(), teamId)) {
            return null;
        }

        Team team = teamService.get(teamId);

        if (!teamName.isEmpty()) {
            return teamService.updateTeamName(teamId, teamName).getName();
        }

        return "layouts/_empty";
    }

//    @GetMapping("/getLink")
//    private String generateLink(Model model) {
//        model.addAttribute("teamsList", userService.getCurrentUser().getUserTeams());
//        model.addAttribute("teamRoles", teamRoleService.getAllRoles());
//        return HTML_FOLDER + "/_genLink";
//    }

    @ResponseBody
    @PostMapping("/getLink")
    private String generateLink(
            @RequestParam int teamRoleId,
            @RequestParam int teamId
    ) throws InterruptedException {
        return linkService.generateLink(teamId, teamRoleId);
    }

    @GetMapping("/invite/{uniqueParam}")
    private String inviteViaLink(
            @PathVariable String uniqueParam,
            HttpSession httpSession
    ) {
        Team team = linkService.inviteViaLink(uniqueParam);
        httpSession.setAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE, team.getId());
        return REDIRECT_ROOT_MAPPING + team.getId();
    }

    @ResponseBody
    @PostMapping("/{teamId}/kickMember")
    private Map<String, String> kickMember(
            @PathVariable int teamId,
            @RequestParam String userId,
            Principal principal,
            Authentication authentication
    ) {

        Map<String, String> resp = new HashMap<>();

        if (!permissionEvaluator.hasPermission(authentication, teamId, "Team", "w") &&
                !teamRoleService.isUserCuratorOfTeam(userService.getCurrentUser(principal).getId(), teamId)) {
            resp.put("kickedMember", "");
            return null;
        }

        User kickedUser = userService.get(userId);
        teamService.kickMember(userId, teamId);

        resp.put("kickedMember", kickedUser.getFirstName() + " " + kickedUser.getLastName());

        return resp;
    }

    @PostMapping("/{teamId}/leaveTeam")
    private String leaveTeam(
            @PathVariable int teamId,
            HttpSession httpSession,
            Principal principal
    ) {

        User currentUser = userService.getCurrentUser(principal);

        teamService.kickMember(currentUser.getId(), teamId);
        httpSession.setAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE, -1);

        return "redirect:/entrance/1";
    }

    @PostMapping("/search")
    public String search(
            @RequestParam String q,
            @RequestParam int shiftId,
            HttpSession httpSession,
            Principal principal,
            Model model
    ) throws InterruptedException {

        PermissionService.Identifier identifier =
                permissionService.identifyUser(shiftService.get(shiftId).getActivity().getId());

        model.addAttribute(identifier.getValue(), true);
        model.addAttribute("teams", teamService.searchByName(q, shiftId));
        model.addAttribute("curShiftId", shiftId);

        return HTML_FOLDER + "/_teams_list";
    }

    @PostMapping("/partialTeams")
    public String partialTeams(
            @RequestParam int shiftId,
            Model model
    ) {

        model.addAttribute("teams", teamService.getTeamModelsByShiftId(shiftId));

        return HTML_FOLDER + "/_teams_list";
    }

}
