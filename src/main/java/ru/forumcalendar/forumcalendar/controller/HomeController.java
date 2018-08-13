package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.forumcalendar.forumcalendar.config.SessionAttributeName;
import ru.forumcalendar.forumcalendar.domain.Team;
import ru.forumcalendar.forumcalendar.domain.TeamRole;
import ru.forumcalendar.forumcalendar.domain.UserTeam;
import ru.forumcalendar.forumcalendar.model.TeamModel;
import ru.forumcalendar.forumcalendar.model.form.ChoosingActivityForm;
import ru.forumcalendar.forumcalendar.model.form.ChoosingShiftForm;
import ru.forumcalendar.forumcalendar.model.form.ChoosingTeamForm;
import ru.forumcalendar.forumcalendar.model.form.ChoosingTeamRoleForm;
import ru.forumcalendar.forumcalendar.service.ActivityService;
import ru.forumcalendar.forumcalendar.service.ShiftService;
import ru.forumcalendar.forumcalendar.service.TeamService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class HomeController {

    private final ActivityService activityService;
    private final ShiftService shiftService;
    private final TeamService teamService;

    @Autowired
    public HomeController(
            ActivityService activityService,
            ShiftService shiftService,
            TeamService teamService
    ) {
        this.activityService = activityService;
        this.shiftService = shiftService;
        this.teamService = teamService;
    }

    @GetMapping("/")
    public String home() {
        return "home/login";
    }

    @GetMapping("/entrance/1")
    public String entranceStepChoosingActivity(
            Model model
    ) {

        model.addAttribute("activities", activityService.getAll());

        return "entrance1_choosing_activity";
    }

    @PostMapping("/entrance/2")
    public String entranceStepChoosingShift(
            Model model,
            @Valid ChoosingActivityForm choosingActivityForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "entrance1_choosing_activity";
        }

        model.addAttribute("shifts", shiftService.getShiftModelsByActivityId(choosingActivityForm.getActivityId()));

        return "entrance2_choosing_shift";
    }

    @PostMapping("/entrance/3")
    public String entranceStepChoosingTeamRole(
            Model model,
            HttpSession httpSession,
            @Valid ChoosingShiftForm choosingShiftForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "entrance2_choosing_shift";
        }

        Integer teamId = shiftService.getCurrentUserTeamByShift(choosingShiftForm.getShiftId());

        if (teamId == null) {
            model.addAttribute("shiftId", choosingShiftForm.getShiftId());
            return "entrance3_choosing_teamrole";
        } else {
            httpSession.setAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE, teamService.get(teamId));
            return "redirect:/menu";
        }
    }

    @PostMapping("/entrance/4")
    public String entranceStepChoosingTeam(
            Model model,
            @Valid ChoosingTeamRoleForm choosingTeamRoleForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "entrance3_choosing_teamrole";
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

        model.addAttribute("teams", teams);
        model.addAttribute("teamRoleId", choosingTeamRoleForm.getTeamRoleId());

        return "entrance4_choosing_team";
    }

    @PostMapping("/entrance/5")
    public String entranceStepFinal(
            HttpSession httpSession,
            @Valid ChoosingTeamForm choosingTeamForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "entrance4_choosing_team";
        }

        UserTeam userTeam = teamService.joinCurrentUserToTeam(choosingTeamForm.getTeamId(), choosingTeamForm.getTeamRoleId());
        httpSession.setAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE, userTeam.getUserTeamIdentity().getTeam());

        return "redirect:/menu";
    }

    @GetMapping("/menu")
    public String menu(
            Model model,
            HttpSession httpSession
    ) {

        Team team = (Team) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);

        if (team == null) {
            return "redirect:/entrance/1";
        }

        model.addAttribute("teamId", team);

        return "home/menu";
    }

    @GetMapping("/menuExit")
    public String menuExit(
            HttpSession httpSession
    ) {

        httpSession.removeAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);

        return "redirect:/entrance/1";
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
