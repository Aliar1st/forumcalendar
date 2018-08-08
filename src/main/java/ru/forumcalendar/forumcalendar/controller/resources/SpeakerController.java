package ru.forumcalendar.forumcalendar.controller.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.forumcalendar.forumcalendar.model.form.ShiftForm;
import ru.forumcalendar.forumcalendar.model.form.SpeakerForm;
import ru.forumcalendar.forumcalendar.service.SpeakerService;

import javax.validation.Valid;

@Controller
@RequestMapping("editor/activity/{activityId}/speaker")
public class SpeakerController {

    private static final String HTML_FOLDER = "editor/speaker/";

    private final SpeakerService speakerService;

    @Autowired
    public SpeakerController(
            SpeakerService speakerService
    ) {
        this.speakerService = speakerService;
    }

    @GetMapping("")
    public String index(
            @PathVariable int activityId,
            Model model
    ) {

        model.addAttribute("speakerModels", speakerService.getSpeakerModelsByActivityId(activityId));

        return HTML_FOLDER + "index";
    }

    @GetMapping("add")
    public String add(
            Model model
    ) {

        model.addAttribute(new SpeakerForm());

        return HTML_FOLDER + "add";
    }

    @PostMapping("add")
    public String add(
            @PathVariable int activityId,
            @Valid SpeakerForm speakerForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "add";
        }

        speakerForm.setActivityId(activityId);
        speakerService.save(speakerForm);

        return "redirect:";
    }

    @GetMapping("{teamId}/edit")
    public String edit(
            @PathVariable int teamId,
            Model model
    ) {

        SpeakerForm speakerForm = new SpeakerForm(speakerService.get(teamId));
        model.addAttribute(speakerForm);

        return HTML_FOLDER + "edit";
    }

    @PostMapping("{teamId}/edit")
    public String edit(
            @PathVariable int teamId,
            @Valid SpeakerForm speakerForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "edit";
        }

        speakerForm.setId(teamId);
        speakerService.save(speakerForm);

        return "redirect:..";
    }

    @GetMapping("{teamId}/delete")
    public String delete(
            @PathVariable int teamId
    ) {

        speakerService.delete(teamId);

        return "redirect:..";
    }
}
