package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.forumcalendar.forumcalendar.model.form.ActivityForm;
import ru.forumcalendar.forumcalendar.service.ActivityService;
import ru.forumcalendar.forumcalendar.service.ShiftService;

import javax.validation.Valid;

@Controller
@RequestMapping("activity")
public class ActivityController {

    private ActivityService activityService;
    private ShiftService shiftService;

    @Autowired
    public ActivityController(
            ActivityService activityService,
            ShiftService shiftService
    ) {
        this.activityService = activityService;
        this.shiftService = shiftService;
    }

    @GetMapping("")
    public String index(Model model) {

        model.addAttribute("activityModels", activityService.getCurrentUserActivityModels());

        return "activity/index";
    }

    @GetMapping("/add")
    public String add(Model model) {

        model.addAttribute(new ActivityForm());

        return "activity/add";
    }

    @PostMapping("/add")
    public String add(
            @Valid ActivityForm activityForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "activity/add";
        }

        activityService.save(activityForm);

        return "redirect:/activity";
    }

    @GetMapping("/{activityId}/edit")
    public String edit(
            @PathVariable Integer activityId,
            Model model
    ) {

        ActivityForm activityForm = new ActivityForm(activityService.get(activityId));
        model.addAttribute(activityForm);

        return "activity/edit";
    }

    @PostMapping("/{activityId}/edit")
    public String edit(
            @PathVariable Integer activityId,
            @Valid ActivityForm activityForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "activity/edit";
        }

        activityForm.setId(activityId);
        activityService.save(activityForm);

        return "redirect:/activity";
    }

    @GetMapping("/{activityId}/delete")
    public String delete(@PathVariable Integer activityId) {

        activityService.delete(activityId);

        return "redirect:/activity";
    }
}
