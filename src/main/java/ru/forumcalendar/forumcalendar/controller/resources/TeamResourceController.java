package ru.forumcalendar.forumcalendar.controller.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.forumcalendar.forumcalendar.model.form.ShiftForm;
import ru.forumcalendar.forumcalendar.model.form.TeamForm;
import ru.forumcalendar.forumcalendar.service.TeamService;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("editor/team")
@PreAuthorize("hasRole('ROLE_SUPERUSER')")
public class TeamResourceController {

    private static final String HTML_FOLDER = "editor/team/";

    private final TeamService teamService;

    @Autowired
    public TeamResourceController(
            TeamService teamService
    ) {
        this.teamService = teamService;
    }

    @GetMapping("")
    public String index(
            @PathVariable int shiftId,
            Model model
    ) {

        model.addAttribute("teamModels", teamService.getTeamModelsByShiftId(shiftId));

        return HTML_FOLDER + "index";
    }

    @GetMapping("add")
    public String add(
            Model model
    ) {

        model.addAttribute(new TeamForm());

        return HTML_FOLDER + "add";
    }

    @PostMapping("add")
    public String add(
            @PathVariable int shiftId,
            @Valid TeamForm teamForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "add";
        }

        teamForm.setShiftId(shiftId);
        teamService.save(teamForm);

        return "redirect:";
    }

    @GetMapping("{teamId}/edit")
    public String edit(
            @PathVariable int teamId,
            Model model
    ) {

        TeamForm teamForm = new TeamForm(teamService.get(teamId));
        model.addAttribute(teamForm);

        return HTML_FOLDER + "edit";
    }

    @PostMapping("{teamId}/edit")
    public String edit(
            @PathVariable int teamId,
            @Valid TeamForm teamForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "edit";
        }

        teamForm.setId(teamId);
        teamService.save(teamForm);

        return "redirect:..";
    }

    @GetMapping("{teamId}/delete")
    public String delete(
            @PathVariable int teamId
    ) {

        teamService.delete(teamId);

        return "redirect:..";
    }
}
