package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.forumcalendar.forumcalendar.config.constt.SessionAttributeName;
import ru.forumcalendar.forumcalendar.model.TeamEventModel;
import ru.forumcalendar.forumcalendar.model.form.TeamEventForm;
import ru.forumcalendar.forumcalendar.service.TeamEventService;
import ru.forumcalendar.forumcalendar.service.TeamMemberStatus;
import ru.forumcalendar.forumcalendar.service.TeamService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/team_events")
public class TeamEventController {

    private static final String HTML_FOLDER = "team_event/";
    private static final String REDIRECT_ROOT_MAPPING = "redirect:/team_events";

    private final TeamService teamService;
    private final TeamEventService teamEventService;
    private final ConversionService conversionService;
    private final PermissionEvaluator permissionEvaluator;

    @Autowired
    public TeamEventController(
            TeamService teamService,
            TeamEventService teamEventService,
            @Qualifier("mvcConversionService") ConversionService conversionService,
            PermissionEvaluator permissionEvaluator
    ) {
        this.teamService = teamService;
        this.teamEventService = teamEventService;
        this.conversionService = conversionService;
        this.permissionEvaluator = permissionEvaluator;
    }

    @GetMapping("")
    public String index(
            Model model,
            HttpSession httpSession,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        int teamId = (int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);

        TeamMemberStatus teamMemberStatus = teamService.getStatus(teamId);
        if (TeamMemberStatus.OK != teamMemberStatus) {
            return teamService.resolveTeamError(teamMemberStatus, httpSession, redirectAttributes);
        }

        if (!permissionEvaluator.hasPermission(authentication, teamService.get(teamId).getShift().getId(), "Shift", "r")) {
            return REDIRECT_ROOT_MAPPING;
        }

        model.addAttribute("teamEvents", teamEventService.getTeamEventModelsByTeamId(teamId));

        return HTML_FOLDER + "index";
    }

    @PreAuthorize("hasPermission(#id, 'Team_event', 'r')")
    @GetMapping("{id}")
    public String show(
            @PathVariable int id,
            Model model
    ) {
        model.addAttribute("event", conversionService.convert(teamEventService.get(id), TeamEventModel.class));

        return HTML_FOLDER + "show";
    }

    @GetMapping("add")
    public String add(
            Model model,
            HttpSession httpSession,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        int teamId = (int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);

        TeamMemberStatus teamMemberStatus = teamService.getStatus(teamId);
        if (TeamMemberStatus.OK != teamMemberStatus) {
            return teamService.resolveTeamError(teamMemberStatus, httpSession, redirectAttributes);
        }

        if (!permissionEvaluator.hasPermission(authentication, teamService.get(teamId).getShift().getId(), "Shift", "r")) {
            return REDIRECT_ROOT_MAPPING;
        }

        TeamEventForm teamEventForm = new TeamEventForm();
        teamEventForm.setTeamId(teamId);

        model.addAttribute(teamEventForm);

        return HTML_FOLDER + "add";
    }

    @PostMapping("add")
    public String add(
            @Valid TeamEventForm teamEventForm,
            BindingResult bindingResult,
            Authentication authentication
    ) {
        if (!permissionEvaluator.hasPermission(authentication, teamService.get(teamEventForm.getTeamId()).getShift().getId(), "Shift", "r")) {
            return REDIRECT_ROOT_MAPPING;
        }

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "add";
        }

        teamEventService.save(teamEventForm);

        return REDIRECT_ROOT_MAPPING;
    }

    @PreAuthorize("hasPermission(#id, 'Team_event', 'w')")
    @GetMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            Model model
    ) {
        TeamEventForm teamEventForm = new TeamEventForm(teamEventService.get(id));
        model.addAttribute(teamEventForm);

        return HTML_FOLDER + "edit";
    }

    @PreAuthorize("hasPermission(#id, 'Team_event', 'w')")
    @PostMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            @Valid TeamEventForm teamEventForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "edit";
        }

        teamEventForm.setId(id);
        teamEventService.save(teamEventForm);

        return REDIRECT_ROOT_MAPPING;
    }

    @PreAuthorize("hasPermission(#id, 'Team_event', 'w')")
    @GetMapping("{id}/delete")
    public String delete(
            @PathVariable int id,
            HttpServletRequest request
    ) {
        teamEventService.delete(id);

        return "redirect:" + request.getHeader("referer");
    }
}
