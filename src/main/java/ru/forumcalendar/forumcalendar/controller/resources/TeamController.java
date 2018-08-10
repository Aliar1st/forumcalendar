package ru.forumcalendar.forumcalendar.controller.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.forumcalendar.forumcalendar.model.form.TeamForm;
import ru.forumcalendar.forumcalendar.service.TeamService;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("editor/activity/{activityId}/shift/{shiftId}/team")
public class TeamController {

    private static final String HTML_FOLDER = "editor/team/";

    private final TeamService teamService;

    @Autowired
    public TeamController(
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
            BindingResult bindingResult,
            Model model
    ) {

        teamForm.setShiftId(shiftId);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            return HTML_FOLDER + "add";
        }

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
            @PathVariable int shiftId,
            @PathVariable int teamId,
            @Valid TeamForm teamForm,
            BindingResult bindingResult,
            Model model
    ) {

        teamForm.setId(teamId);
        teamForm.setShiftId(shiftId);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            return HTML_FOLDER + "edit";
        }

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
