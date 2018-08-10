package ru.forumcalendar.forumcalendar.controller.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.forumcalendar.forumcalendar.model.form.EventForm;
import ru.forumcalendar.forumcalendar.repository.SpeakerRepository;
import ru.forumcalendar.forumcalendar.service.EventService;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("editor/activity/{activityId}/shift/{shiftId}/event")
public class EventController {

    private static final String HTML_FOLDER = "editor/event/";

    private final SpeakerRepository speakerRepository;
    private final EventService eventService;

    @Autowired
    public EventController(
            SpeakerRepository speakerRepository,
            EventService eventService
    ) {
        this.speakerRepository = speakerRepository;
        this.eventService = eventService;
    }

    @GetMapping("")
    public String index(
            @PathVariable int activityId,
            @PathVariable int shiftId,
            Model model
    ) {

        model.addAttribute("eventModels", eventService.getEventModelsByShiftId(shiftId));

        return HTML_FOLDER + "index";
    }

    @GetMapping("add")
    public String add(
            @PathVariable int activityId,
            Model model
    ) {

        model.addAttribute(new EventForm());
        model.addAttribute("speakers", speakerRepository.getAllByActivityIdOrderByCreatedAt(activityId));

        return HTML_FOLDER + "add";
    }

    @PostMapping("add")
    public String add(
            @PathVariable int activityId,
            @Valid EventForm eventForm,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("speakers", speakerRepository.getAllByActivityIdOrderByCreatedAt(activityId));
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            return HTML_FOLDER + "add";
        }

        eventService.save(eventForm);

        return "redirect:";
    }

    @GetMapping("{eventId}/edit")
    public String edit(
            @PathVariable int eventId,
            Model model
    ) {

        EventForm eventForm = new EventForm(eventService.get(eventId));
        model.addAttribute(eventForm);

        return HTML_FOLDER + "edit";
    }

    @PostMapping("{eventId}/edit")
    public String edit(
            @PathVariable int eventId,
            @Valid EventForm eventForm,
            BindingResult bindingResult,
            Model model
    ) {

        eventForm.setId(eventId);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            return HTML_FOLDER + "edit";
        }

        eventService.save(eventForm);

        return "redirect:..";
    }

    @GetMapping("{eventId}/delete")
    public String delete(
            @PathVariable int eventId
    ) {

        eventService.delete(eventId);

        return "redirect:..";
    }
}
