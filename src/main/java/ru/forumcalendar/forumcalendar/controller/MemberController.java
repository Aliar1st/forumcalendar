package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.forumcalendar.forumcalendar.service.UserService;

@Controller
@RequestMapping("/member")
public class MemberController {

    private static final String HTML_FOLDER = "member/";

    private final UserService userService;

    @Autowired
    public MemberController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/activity")
    public String activityIndex(
            Model model,
            @RequestParam int activityId
    ) {
        model.addAttribute("curators", userService.getAllCuratorsByActivityId(activityId));
        model.addAttribute("users", userService.getAllNotCuratorsByActivityId(activityId));

        return HTML_FOLDER + "/index";
    }

    @GetMapping("/shift")
    public String shiftIndex(
            Model model,
            @RequestParam int shiftId
    ) {
        model.addAttribute("curators", userService.getAllCuratorsByShiftId(shiftId));
        model.addAttribute("users", userService.getAllNotCuratorsByShiftId(shiftId));

        return HTML_FOLDER + "/index";
    }
}
