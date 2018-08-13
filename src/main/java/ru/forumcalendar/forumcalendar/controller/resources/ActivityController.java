package ru.forumcalendar.forumcalendar.controller.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.forumcalendar.forumcalendar.model.form.ActivityForm;
import ru.forumcalendar.forumcalendar.service.ActivityService;

import javax.validation.Valid;
import java.util.List;

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

    @PreAuthorize("@baseActivityService.hasPermissionToWrite(#activityId) or hasRole('SUPERUSER')")
    @GetMapping("{activityId}")
    public String show(
            @P("activityId") @PathVariable int activityId,
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

    @PreAuthorize("@baseActivityService.hasPermissionToWrite(#activityId) or hasRole('SUPERUSER')")
    @GetMapping("{activityId}/edit")
    public String edit(
            @P("activityId") @PathVariable int activityId,
            Model model
    ) {

        model.addAttribute(new ActivityForm(activityService.get(activityId)));

        return HTML_FOLDER + "edit";
    }

    @PreAuthorize("@baseActivityService.hasPermissionToWrite(#activityId) or hasRole('SUPERUSER')")
    @PostMapping("{activityId}/edit")
    public String edit(
            @P("activityId") @PathVariable int activityId,
            @Valid ActivityForm activityForm,
            BindingResult bindingResult
    ) {

        activityForm.setId(activityId);
        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "edit";
        }

        activityService.save(activityForm);

        return "redirect:..";
    }

    @PreAuthorize("@baseActivityService.hasPermissionToWrite(#activityId) or hasRole('SUPERUSER')")
    @GetMapping("{activityId}/delete")
    public String delete(@P("activityId") @PathVariable int activityId) {

        activityService.delete(activityId);

        return "redirect:..";
    }
}
