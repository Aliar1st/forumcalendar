package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.model.ActivityModel;
import ru.forumcalendar.forumcalendar.model.form.ActivityForm;
import ru.forumcalendar.forumcalendar.service.ActivityService;
import ru.forumcalendar.forumcalendar.service.PermissionService;

import javax.validation.Valid;

@Controller
@RequestMapping("activities")
public class ActivityController {

    private static final String HTML_FOLDER = "activity/";
    private static final String REDIRECT_ROOT_MAPPING = "redirect:/activities/";

    private final ConversionService conversionService;
    private final PermissionService permissionService;
    private final ActivityService activityService;

    private final PermissionEvaluator permissionEvaluator;

    @Autowired
    public ActivityController(
            ConversionService conversionService,
            PermissionService permissionService,
            ActivityService activityService,
            PermissionEvaluator permissionEvaluator
    ) {
        this.conversionService = conversionService;
        this.permissionService = permissionService;
        this.activityService = activityService;
        this.permissionEvaluator = permissionEvaluator;
    }

    @GetMapping("{id}")
    public String activity(
            @PathVariable int id,
            Model model
    ) {

        PermissionService.Identifier identifier = permissionService.identifyUser(id);

        model.addAttribute(identifier.getValue(), true);
        model.addAttribute("activity", conversionService.convert(activityService.get(id), ActivityModel.class));

        return HTML_FOLDER + "activity";
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    public String index(Model model) {

        model.addAttribute("activities", activityService.getAll());

        return HTML_FOLDER + "activities";
    }

    @GetMapping("add")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    public String add(
            Model model
    ) {

        model.addAttribute(new ActivityForm());

        return HTML_FOLDER + "add";
    }

    @PostMapping("add")
    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    public String add(
            @Valid ActivityForm activityForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "add";
        }

        Activity activity = activityService.save(activityForm);

        return REDIRECT_ROOT_MAPPING + activity.getId();
    }


    @GetMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            Model model,
            Authentication authentication
    ) {
        if (!permissionEvaluator.hasPermission(authentication, id, "Activity", "w")) {
            return REDIRECT_ROOT_MAPPING + id;
        }

        model.addAttribute(new ActivityForm(activityService.get(id)));

        return HTML_FOLDER + "edit";
    }

    @PostMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            @Valid ActivityForm activityForm,
            BindingResult bindingResult,
            Authentication authentication
    ) {
        if (!permissionEvaluator.hasPermission(authentication, id, "Activity", "w")) {
            return REDIRECT_ROOT_MAPPING + id;
        }

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "edit";
        }

        activityForm.setId(id);
        activityService.save(activityForm);

        return REDIRECT_ROOT_MAPPING + id;
    }

    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    @PostMapping("/search")
    public String search(
            @RequestParam String q,
            Model model
    ) throws InterruptedException {

        model.addAttribute("activities", activityService.searchByName(q));

        return HTML_FOLDER + "_activities_list";
    }

    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    @PostMapping("/partialActivities")
    public String partialTeams(
            Model model
    ) {

        model.addAttribute("activities", activityService.getAll());

        return HTML_FOLDER + "/_activities_list";
    }

    @PreAuthorize("hasRole('ROLE_SUPERUSER')")
    @PostMapping("getShiftForm")
    public String getShiftForm(
            @RequestParam int num,
            Model model
    ) {

        model.addAttribute("num", num);

        return HTML_FOLDER + "_add_shift";
    }
}

