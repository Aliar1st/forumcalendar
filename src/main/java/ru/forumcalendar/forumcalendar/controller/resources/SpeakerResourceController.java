package ru.forumcalendar.forumcalendar.controller.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
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
public class SpeakerResourceController {

    private static final String HTML_FOLDER = "editor/speaker/";

    private final SpeakerService speakerService;

    @Autowired
    public SpeakerResourceController(
            SpeakerService speakerService
    ) {
        this.speakerService = speakerService;
    }

    @PreAuthorize("@baseActivityService.hasPermissionToWrite(#activityId) or hasRole('SUPERUSER')")
    @GetMapping("")
    public String index(
            @P("activityId") @PathVariable int activityId,
            Model model
    ) {

        model.addAttribute("speakerModels", speakerService.getSpeakerModelsByActivityId(activityId));

        return HTML_FOLDER + "index";
    }

    @PreAuthorize("@baseActivityService.hasPermissionToWrite(#activityId) or hasRole('SUPERUSER')")
    @GetMapping("add")
    public String add(
            @P("activityId") @PathVariable int activityId,
            Model model
    ) {

        model.addAttribute(new SpeakerForm());

        return HTML_FOLDER + "add";
    }

    @PreAuthorize("@baseActivityService.hasPermissionToWrite(#activityId) or hasRole('SUPERUSER')")
    @PostMapping("add")
    public String add(
            @P("activityId") @PathVariable int activityId,
            @Valid SpeakerForm speakerForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "add";
        }

        speakerService.save(speakerForm);

        return "redirect:";
    }

    @PreAuthorize("@baseSpeakerService.isUserSpeaker(#speakerId) or hasRole('SUPERUSER')")
    @GetMapping("{speakerId}/edit")
    public String edit(
            @P("speakerId") @PathVariable int speakerId,
            Model model
    ) {

        SpeakerForm speakerForm = new SpeakerForm(speakerService.get(speakerId));
        model.addAttribute(speakerForm);

        return HTML_FOLDER + "edit";
    }

    @PreAuthorize("@baseSpeakerService.isUserSpeaker(#speakerId) or hasRole('SUPERUSER')")
    @PostMapping("{speakerId}/edit")
    public String edit(
            @P("speakerId") @PathVariable int speakerId,
            @Valid SpeakerForm speakerForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "edit";
        }

        speakerForm.setId(speakerId);
        speakerService.save(speakerForm);

        return "redirect:..";
    }

    @PreAuthorize("@baseSpeakerService.isUserSpeaker(#speakerId) or hasRole('SUPERUSER')")
    @GetMapping("{speakerId}/delete")
    public String delete(
            @P("speakerId") @PathVariable int speakerId
    ) {

        speakerService.delete(speakerId);

        return "redirect:..";
    }
}
