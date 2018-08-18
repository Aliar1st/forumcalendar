package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.forumcalendar.forumcalendar.service.PermissionService;
import ru.forumcalendar.forumcalendar.service.SpeakerService;
import ru.forumcalendar.forumcalendar.service.UserService;

@Controller
@RequestMapping("/speaker")
public class SpeakerController {

    private static final String HTML_FOLDER = "speaker/";

    private final UserService userService;
    private final SpeakerService speakerService;
    private final PermissionService permissionService;

    @Autowired
    public SpeakerController(
            UserService userService,
            SpeakerService speakerService,
            PermissionService permissionService
    ) {
        this.userService = userService;
        this.speakerService = speakerService;
        this.permissionService = permissionService;
    }

    @GetMapping("/speakers")
    public String speakers(
            @RequestParam int activityId,
            Authentication authentication,
            Model model
    ) {

        String perm = permissionService.identifyUser(authentication, activityId, "Activity", "w");

        model.addAttribute(perm, true);
        model.addAttribute("curActivityId", activityId);
        model.addAttribute("speakers", speakerService.getSpeakerModelsByActivityId(activityId));

        return HTML_FOLDER + "speakers";
    }

}
