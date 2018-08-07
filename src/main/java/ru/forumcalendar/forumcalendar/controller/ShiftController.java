package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.forumcalendar.forumcalendar.service.ActivityService;
import ru.forumcalendar.forumcalendar.service.ShiftService;

@Controller
@RequestMapping("activity/{activityId}")
public class ShiftController {

    private ShiftService shiftService;

    @Autowired
    public ShiftController(
            ShiftService shiftService
    ) {
        this.shiftService = shiftService;
    }

    @GetMapping("")
    public String index(
            @PathVariable Integer activityId,
            Model model
    ) {

        model.addAttribute("shiftModels", shiftService.getShiftModelsByActivityId(activityId));

        return "shift/index";
    }
}
