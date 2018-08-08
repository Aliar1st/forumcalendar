package ru.forumcalendar.forumcalendar.controller.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.forumcalendar.forumcalendar.model.form.ActivityForm;
import ru.forumcalendar.forumcalendar.service.ActivityService;

import javax.validation.Valid;

@Controller
@RequestMapping("editor/activity")
public class ActivityController {

    private static final String HTML_FOLDER = "editor/activity/";

    private final ActivityService activityService;

    @Autowired
    public ActivityController(
            ActivityService activityService
    ) {
        this.activityService = activityService;
    }

    @GetMapping("")
    public String index(Model model) {

        model.addAttribute("activityModels", activityService.getCurrentUserActivityModels());

        return HTML_FOLDER + "index";
    }

    @GetMapping("{activityId}")
    public String show(
            @PathVariable int activityId,
            Model model
    ) {

        model.addAttribute(activityId);

        return HTML_FOLDER + "show";
    }

    @GetMapping("add")
    public String add(Model model) {

        model.addAttribute(new ActivityForm());

        return HTML_FOLDER + "add";
    }

    @PostMapping("add")
    public String add(
            @Valid ActivityForm activityForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "add";
        }

        activityService.save(activityForm);

        return "redirect:";
    }

    @GetMapping("{activityId}/edit")
    public String edit(
            @PathVariable int activityId,
            Model model
    ) {

        model.addAttribute(new ActivityForm(activityService.get(activityId)));

        return HTML_FOLDER + "edit";
    }

    @PostMapping("{activityId}/edit")
    public String edit(
            @PathVariable int activityId,
            @Valid ActivityForm activityForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "edit";
        }

        activityForm.setId(activityId);
        activityService.save(activityForm);

        return "redirect:..";
    }

    @GetMapping("{activityId}/delete")
    public String delete(@PathVariable int activityId) {

        activityService.delete(activityId);

        return "redirect:..";
    }
}
