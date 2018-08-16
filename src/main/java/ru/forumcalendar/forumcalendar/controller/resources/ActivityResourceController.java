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
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("editor/activity")
@PreAuthorize("hasRole('ROLE_SUPERUSER')")
public class ActivityResourceController {

    private static final String HTML_FOLDER = "editor/activity/";

    private final ActivityService activityService;

    @Autowired
    public ActivityResourceController(
            ActivityService activityService
    ) {
        this.activityService = activityService;
    }

    @GetMapping("")
    public String index(Model model) {

        model.addAttribute("activityModels", activityService.getCurrentUserActivityModels());

        return HTML_FOLDER + "index";
    }

    @GetMapping("{id}")
    public String show(
            @PathVariable int id,
            Model model
    ) {

        model.addAttribute(id);

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

    @GetMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            Model model
    ) {

        model.addAttribute(new ActivityForm(activityService.get(id)));

        return HTML_FOLDER + "edit";
    }

    @PostMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            @Valid ActivityForm activityForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "edit";
        }

        activityForm.setId(id);
        activityService.save(activityForm);

        return "redirect:..";
    }

    @GetMapping("{id}/delete")
    public String delete(@PathVariable int id) {

        activityService.delete(id);

        return "redirect:..";
    }
}
