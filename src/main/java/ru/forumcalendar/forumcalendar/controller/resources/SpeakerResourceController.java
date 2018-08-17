package ru.forumcalendar.forumcalendar.controller.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.forumcalendar.forumcalendar.domain.Speaker;
import ru.forumcalendar.forumcalendar.model.form.SpeakerForm;
import ru.forumcalendar.forumcalendar.service.SpeakerService;

import javax.validation.Valid;

@Controller
@RequestMapping("editor/speaker")
@PreAuthorize("hasRole('ROLE_SUPERUSER')")
public class SpeakerResourceController {

    private static final String HTML_FOLDER = "editor/speaker/";
    private static final String REDIRECT_ROOT_MAPPING = "redirect:/editor/speaker?activityId=";

    private final SpeakerService speakerService;

    @Autowired
    public SpeakerResourceController(
            SpeakerService speakerService
    ) {
        this.speakerService = speakerService;
    }

    @GetMapping("")
    public String index(
            @RequestParam int activityId,
            Model model
    ) {
        model.addAttribute("speakers", speakerService.getSpeakerModelsByActivityId(activityId));
        model.addAttribute("activityId", activityId);

        return HTML_FOLDER + "index";
    }

    @GetMapping("add")
    public String add(
            @RequestParam int activityId,
            Model model
    ) {
        SpeakerForm speakerForm = new SpeakerForm();
        speakerForm.setActivityId(activityId);

        model.addAttribute(speakerForm);

        return HTML_FOLDER + "add";
    }

    @PostMapping("add")
    public String add(
            @Valid SpeakerForm speakerForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "add";
        }

        speakerService.save(speakerForm);

        return REDIRECT_ROOT_MAPPING + speakerForm.getActivityId();
    }

    @GetMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            Model model
    ) {
        SpeakerForm speakerForm = new SpeakerForm(speakerService.get(id));
        model.addAttribute(speakerForm);

        return HTML_FOLDER + "edit";
    }

    @PostMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            @Valid SpeakerForm speakerForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "edit";
        }

        speakerForm.setId(id);
        speakerService.save(speakerForm);

        return REDIRECT_ROOT_MAPPING + speakerForm.getActivityId();
    }

    @GetMapping("{id}/delete")
    public String delete(
            @PathVariable int id
    ) {
        Speaker speaker = speakerService.delete(id);

        return REDIRECT_ROOT_MAPPING + speaker.getActivity().getId();
    }
}
