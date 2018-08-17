package ru.forumcalendar.forumcalendar.controller;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.forumcalendar.forumcalendar.config.constt.SessionAttributeName;
import ru.forumcalendar.forumcalendar.domain.Role;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.domain.Team;
import ru.forumcalendar.forumcalendar.domain.User;
import ru.forumcalendar.forumcalendar.service.ActivityService;
import ru.forumcalendar.forumcalendar.service.SpeakerService;
import ru.forumcalendar.forumcalendar.service.TeamService;
import ru.forumcalendar.forumcalendar.service.UserService;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class KFCController {

    private static final String HTML_FOLDER = "teams/";

    private final UserService userService;
    private final ActivityService activityService;
    private final SpeakerService speakerService;
    private final TeamService teamService;

    @Autowired
    public KFCController(
            UserService userService,
            ActivityService activityService,
            SpeakerService speakerService,
            TeamService teamService
    ) {
        this.userService = userService;
        this.activityService = activityService;
        this.speakerService = speakerService;
        this.teamService = teamService;
    }

    @GetMapping("/kfc")
    public String favorites(
            HttpSession httpSession,
            Principal principal,
            Model model
    ) throws SchedulerException {

        Team team = teamService.get((int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE));
        Shift currentShift = team.getShift();

        User user = userService.getCurrentUser(principal);

        if (user.getRole().getId() == Role.ROLE_SUPERUSER_ID) {
            //TODO настроить вывод для суперюзеров
        } else {
            model.addAttribute("teams", teamService.getTeamModelsByShiftId(currentShift.getId()));
            model.addAttribute("speakers", speakerService.getSpeakerModelsByActivityId(currentShift.getActivity().getId()));
            model.addAttribute("forums", activityService.getAll());
        }

        return HTML_FOLDER + "index";
    }
}
