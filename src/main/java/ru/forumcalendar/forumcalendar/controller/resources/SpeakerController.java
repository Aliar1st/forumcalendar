package ru.forumcalendar.forumcalendar.controller.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.forumcalendar.forumcalendar.model.form.SpeakerForm;
import ru.forumcalendar.forumcalendar.service.SpeakerService;

import javax.validation.Valid;
import java.util.Map;

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
            @Valid SpeakerForm speakerForm,
            BindingResult bindingResult,
            Model model

    ) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            return HTML_FOLDER + "add";
        }

        speakerService.save(speakerForm);

        return "redirect:";
    }

    @GetMapping("{speakerId}/edit")
    public String edit(
            @PathVariable int speakerId,
            Model model
    ) {

        SpeakerForm speakerForm = new SpeakerForm(speakerService.get(speakerId));
        model.addAttribute(speakerForm);

        return HTML_FOLDER + "edit";
    }

    @PostMapping("{speakerId}/edit")
    public String edit(
            @PathVariable int speakerId,
            @Valid SpeakerForm speakerForm,
            BindingResult bindingResult,
            Model model
    ) {

        speakerForm.setId(speakerId);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            return HTML_FOLDER + "edit";
        }

        speakerForm.setId(speakerId);
        speakerService.save(speakerForm);

        return "redirect:..";
    }

    @GetMapping("{speakerId}/delete")
    public String delete(
            @PathVariable int speakerId
    ) {

        speakerService.delete(speakerId);

        return "redirect:..";
    }
}
