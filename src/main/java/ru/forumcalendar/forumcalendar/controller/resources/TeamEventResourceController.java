package ru.forumcalendar.forumcalendar.controller.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.forumcalendar.forumcalendar.domain.TeamEvent;
import ru.forumcalendar.forumcalendar.model.form.TeamEventForm;
import ru.forumcalendar.forumcalendar.service.TeamEventService;

import javax.validation.Valid;

@Controller
@RequestMapping("editor/team_event")
@PreAuthorize("hasRole('ROLE_SUPERUSER')")
public class TeamEventResourceController {

    private static final String HTML_FOLDER = "editor/team_event/";
    private static final String REDIRECT_ROOT_MAPPING = "redirect:/editor/team_event?teamId=";

    private final TeamEventService teamEventService;

    @Autowired
    public TeamEventResourceController(
            TeamEventService teamEventService
    ) {
        this.teamEventService = teamEventService;
    }

    @GetMapping("")
    public String index(
            @RequestParam int teamId,
            Model model
    ) {
        model.addAttribute("teamEvents", teamEventService.getTeamEventModelsByTeamId(teamId));
        model.addAttribute("teamId", teamId);

        return HTML_FOLDER + "index";
    }

    @GetMapping("add")
    public String add(
            @RequestParam int teamId,
            Model model
    ) {
        TeamEventForm teamEventForm = new TeamEventForm();
        teamEventForm.setTeamId(teamId);

        model.addAttribute(teamEventForm);

        return HTML_FOLDER + "add";
    }

    @PostMapping("add")
    public String add(
            @Valid TeamEventForm teamEventForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "add";
        }

        teamEventService.save(teamEventForm);

        return REDIRECT_ROOT_MAPPING + teamEventForm.getTeamId();
    }

    @GetMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            Model model
    ) {
        TeamEventForm teamEventForm = new TeamEventForm(teamEventService.get(id));
        model.addAttribute(teamEventForm);

        return HTML_FOLDER + "edit";
    }

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

        return REDIRECT_ROOT_MAPPING + teamEventForm.getTeamId();
    }

    @GetMapping("{id}/delete")
    public String delete(
            @PathVariable int id
    ) {
        TeamEvent teamEvent = teamEventService.delete(id);

        return REDIRECT_ROOT_MAPPING + teamEvent.getTeam().getId();
    }
}
