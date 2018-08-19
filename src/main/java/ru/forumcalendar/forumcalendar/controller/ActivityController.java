package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.forumcalendar.forumcalendar.service.ActivityService;
import ru.forumcalendar.forumcalendar.service.PermissionService;

@Controller
@RequestMapping("activities")
public class ActivityController {

    private static final String HTML_FOLDER = "activity/";

    private final ActivityService activityService;
    private final PermissionService permissionService;

    @Autowired
    public ActivityController(
            ActivityService activityService,
            PermissionService permissionService
    ) {
        this.activityService = activityService;
        this.permissionService = permissionService;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    public String index(Model model) {

        model.addAttribute("forums", activityService.getAll());

        return HTML_FOLDER + "activities";
    }


//    @GetMapping("/activity/activities/search")
//    public String search(
//            @RequestParam String query,
//            HttpSession httpSession,
//            Principal principal,
//            Model model
//    ) throws SchedulerException, InterruptedException {
//
//        //Team team = teamService.get((int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE));
//
//        teamService.searchByName(query);
//
//        return HTML_FOLDER + "index";
//    }

}

