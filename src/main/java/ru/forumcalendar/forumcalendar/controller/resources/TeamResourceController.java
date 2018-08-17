package ru.forumcalendar.forumcalendar.controller.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.forumcalendar.forumcalendar.domain.Team;
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
    private static final String ROOT_MAPPING = "/editor/team?shiftId=";

    private final TeamService teamService;

    @Autowired
    public TeamResourceController(
            TeamService teamService
    ) {
        this.teamService = teamService;
    }

    @GetMapping("")
    public String index(
            @RequestParam int shiftId,
            Model model
    ) {

        model.addAttribute("teams", teamService.getTeamModelsByShiftId(shiftId));
        model.addAttribute("shiftId", shiftId);

        return HTML_FOLDER + "index";
    }

    @GetMapping("{id}")
    public String show(
            @PathVariable int id,
            Model model
    ) {
        model.addAttribute(id);
        model.addAttribute("shiftId", teamService.get(id).getShift().getId());

        return HTML_FOLDER + "show";
    }

    @GetMapping("add")
    public String add(
            @RequestParam int shiftId,
            Model model
    ) {
        TeamForm teamForm = new TeamForm();
        teamForm.setShiftId(shiftId);

        model.addAttribute(teamForm);

        return HTML_FOLDER + "add";
    }

    @PostMapping("add")
    public String add(
            @Valid TeamForm teamForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "add";
        }

        teamService.save(teamForm);

        return "redirect:" + ROOT_MAPPING + teamForm.getShiftId();
    }

    @GetMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            Model model
    ) {
        TeamForm teamForm = new TeamForm(teamService.get(id));
        model.addAttribute(teamForm);

        return HTML_FOLDER + "edit";
    }

    @PostMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            @Valid TeamForm teamForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "edit";
        }

        teamForm.setId(id);
        teamService.save(teamForm);

        return "redirect:" + ROOT_MAPPING + teamForm.getShiftId();
    }

    @GetMapping("{id}/delete")
    public String delete(
            @PathVariable int id
    ) {
        Team team = teamService.delete(id);

        return "redirect:" + ROOT_MAPPING + team.getShift().getId();
    }
}
