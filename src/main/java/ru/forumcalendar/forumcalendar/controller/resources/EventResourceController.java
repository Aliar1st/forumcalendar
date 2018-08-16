package ru.forumcalendar.forumcalendar.controller.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.forumcalendar.forumcalendar.model.EventModel;
import ru.forumcalendar.forumcalendar.model.form.EventForm;
import ru.forumcalendar.forumcalendar.repository.SpeakerRepository;
import ru.forumcalendar.forumcalendar.service.EventService;
import ru.forumcalendar.forumcalendar.service.SpeakerService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("editor/event")
@PreAuthorize("hasRole('ROLE_SUPERUSER')")
public class EventResourceController {

    private static final String HTML_FOLDER = "editor/event/";

    private final SpeakerService speakerService;
    private final EventService eventService;

    @Autowired
    public EventResourceController(
            SpeakerService speakerService,
            EventService eventService
    ) {
        this.speakerService = speakerService;
        this.eventService = eventService;
    }

    @GetMapping("")
    public String index(
            @RequestParam(required = false) Integer shiftId,
            Model model
    ) {
        List<EventModel> events;

        if (shiftId != null) {
            events = eventService.getEventModelsByShiftId(shiftId);
        } else {
            events = eventService.getAll();
        }

        model.addAttribute("events", events);

        return HTML_FOLDER + "index";
    }

    @GetMapping("add")
    public String add(
            @RequestParam int activityId,
            Model model
    ) {
        model.addAttribute(new EventForm());
        model.addAttribute("speakers", speakerService.getSpeakerModelsByActivityId(activityId));

        return HTML_FOLDER + "add";
    }

    @PostMapping("add")
    public String add(
            @Valid EventForm eventForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "add";
        }

        eventService.save(eventForm);

        return "redirect:";
    }

    @GetMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            Model model
    ) {
        EventForm eventForm = new EventForm(eventService.get(id));
        model.addAttribute(eventForm);

        return HTML_FOLDER + "edit";
    }

    @PostMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            @Valid EventForm eventForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "edit";
        }

        eventForm.setId(id);
        eventService.save(eventForm);

        return "redirect:..";
    }

    @GetMapping("{id}/delete")
    public String delete(
            @PathVariable int id
    ) {
        eventService.delete(id);

        return "redirect:..";
    }
}
