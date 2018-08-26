package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.forumcalendar.forumcalendar.config.constt.SessionAttributeName;
import ru.forumcalendar.forumcalendar.domain.*;
import ru.forumcalendar.forumcalendar.model.ActivityModel;
import ru.forumcalendar.forumcalendar.model.ShiftListModel;
import ru.forumcalendar.forumcalendar.model.ShiftModel;
import ru.forumcalendar.forumcalendar.model.TeamModel;
import ru.forumcalendar.forumcalendar.model.form.ChoosingActivityForm;
import ru.forumcalendar.forumcalendar.model.form.ChoosingShiftForm;
import ru.forumcalendar.forumcalendar.model.form.ChoosingTeamForm;
import ru.forumcalendar.forumcalendar.model.form.ChoosingTeamRoleForm;
import ru.forumcalendar.forumcalendar.service.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class HomeController {

    public static final String REDIRECT_TO_ENTRANCE = "redirect:/entrance/1";

    private static final String HTML_FOLDER = "home/";

    private final PermissionService permissionService;
    private final ActivityService activityService;
    private final ShiftService shiftService;
    private final TeamService teamService;
    private final UserService userService;

    private final PermissionEvaluator permissionEvaluator;


    @Autowired
    public HomeController(
            PermissionService permissionService,
            ActivityService activityService,
            ShiftService shiftService,
            TeamService teamService,
            UserService userService,
            PermissionEvaluator permissionEvaluator) {
        this.permissionService = permissionService;
        this.activityService = activityService;
        this.shiftService = shiftService;
        this.teamService = teamService;
        this.userService = userService;
        this.permissionEvaluator = permissionEvaluator;
    }

    @GetMapping("/")
    public String home() {
        return "home/login";
    }

    @GetMapping("/entrance/1")
    public String entranceStepChoosingActivity(
            Model model
    ) {

        PermissionService.Identifier identifier = permissionService.identifyUser();

        model.addAttribute(identifier.getValue(), true);
        model.addAttribute("activities", activityService.getAll());
        model.addAttribute(new ChoosingActivityForm());

        return HTML_FOLDER + "entrance1_choosing_activity";
    }

    @PostMapping("/entrance/2")
    public String entranceStepChoosingShift(
            Model model,
            @Valid ChoosingActivityForm choosingActivityForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return REDIRECT_TO_ENTRANCE;
        }

        List<ShiftListModel> a = shiftService.getShiftModelsByActivityId(choosingActivityForm.getActivityId());

        PermissionService.Identifier identifier = permissionService.identifyUser();

        model.addAttribute(identifier.getValue(), true);
        model.addAttribute("shifts", shiftService.getShiftModelsByActivityId(choosingActivityForm.getActivityId()));
        model.addAttribute(new ChoosingShiftForm());

        return HTML_FOLDER + "entrance2_choosing_shift";
    }

    @PostMapping("/entrance/3")
    public String entranceStepChoosingTeamRole(
            Model model,
            HttpSession httpSession,
            @Valid ChoosingShiftForm choosingShiftForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return REDIRECT_TO_ENTRANCE;
        }

        Integer teamId = shiftService.getCurrentUserTeamByShift(choosingShiftForm.getShiftId());

        if (teamId == null) {
            model.addAttribute("shiftId", choosingShiftForm.getShiftId());
            model.addAttribute(new ChoosingTeamRoleForm());
            return HTML_FOLDER + "entrance3_choosing_teamrole";
        } else {
            httpSession.setAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE, teamId);
            return "redirect:/menu";
        }
    }

    @PostMapping("/entrance/4")
    public String entranceStepChoosingTeam(
            Model model,
            @Valid ChoosingTeamRoleForm choosingTeamRoleForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return REDIRECT_TO_ENTRANCE;
        }

        List<TeamModel> teams;

        switch (choosingTeamRoleForm.getTeamRoleId()) {
            case TeamRole.ROLE_CURATOR_ID:
                teams = teamService.getTeamModelsWithoutCuratorByShiftId(choosingTeamRoleForm.getShiftId());
                break;
            default:
                teams = teamService.getTeamModelsByShiftId(choosingTeamRoleForm.getShiftId());
                break;
        }

        PermissionService.Identifier identifier = permissionService.identifyUser();

        model.addAttribute(identifier.getValue(), true);
        model.addAttribute("teams", teams);
        model.addAttribute("teamRoleId", choosingTeamRoleForm.getTeamRoleId());
        model.addAttribute(new ChoosingTeamForm());

        return HTML_FOLDER + "entrance4_choosing_team";
    }

    @PostMapping("/entrance/5")
    public String entranceStepFinal(
            HttpSession httpSession,
            @Valid ChoosingTeamForm choosingTeamForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return REDIRECT_TO_ENTRANCE;
        }

        UserTeam userTeam = teamService.joinCurrentUserToTeam(choosingTeamForm.getTeamId(), choosingTeamForm.getTeamRoleId());
        httpSession.setAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE, userTeam.getUserTeamIdentity().getTeam().getId());

        return "redirect:/menu";
    }

    @GetMapping("/menu")
    public String menu(
            Model model,
            Authentication authentication,
            HttpSession httpSession,
            RedirectAttributes redirectAttributes
    ) {

        int teamId = (int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);

        TeamMemberStatus teamMemberStatus = teamService.getStatus(teamId);
        if (TeamMemberStatus.OK != teamMemberStatus) {
            return teamService.resolveTeamError(teamMemberStatus, httpSession, redirectAttributes);
        }

        Team team = teamService.get(teamId);

        User user = userService.getCurrentUser();

        PermissionService.Identifier identifier = permissionService.identifyUser(team.getShift().getActivity().getId());

        model.addAttribute(identifier.getValue(), true);

        model.addAttribute("curActivityId", team.getShift().getActivity().getId());
        model.addAttribute("curShiftId", team.getShift().getId());
        model.addAttribute("myTeamId", team.getId());
        model.addAttribute("userId", user.getId());
        model.addAttribute("userPhoto", user.getPhoto());
        model.addAttribute("userName", user.getFirstName() + ' ' + user.getLastName());
        model.addAttribute("teamName", team.getName());

        return HTML_FOLDER + "menu";
    }

    @GetMapping("/menu_exit")
    public String menuExit(
            HttpSession httpSession
    ) {
        httpSession.removeAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);
        return REDIRECT_TO_ENTRANCE;
    }

//    @PostMapping("/getShifts")
//    @ResponseBody
//    public Map<Integer, String> getShifts(@RequestParam int activityId) {
//        return shiftService.getShiftIdNameMapByActivityId(activityId);
//    }
//
//    @PostMapping("/getTeams")
//    @ResponseBody
//    public Map<Integer, String> getTeams(@RequestParam int shiftId) {
//
//        Integer userTeam = shiftService.getCurrentUserTeamByShift(shiftId);
//        Map<String, Object> map = new HashMap<>();
//
//        if (userTeam != null) {
//            map.put()
//        } else {
//
//        }
//        map.put("teams", teamService.getTeamIdNameMapByShiftId(shiftId));
//
//        return teamService.getTeamIdNameMapByShiftId(shiftId);
//    }
}

// TODO: 8/13/2018 Проверка куратор вбивает команду с уже существующим куратором
// TODO: 8/13/2018 Нормальные страницы с ошибками
