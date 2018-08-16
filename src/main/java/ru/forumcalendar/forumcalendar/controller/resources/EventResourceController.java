package ru.forumcalendar.forumcalendar.controller.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.forumcalendar.forumcalendar.model.EventModel;
import ru.forumcalendar.forumcalendar.model.form.EventForm;
import ru.forumcalendar.forumcalendar.repository.SpeakerRepository;
import ru.forumcalendar.forumcalendar.service.EventService;
import ru.forumcalendar.forumcalendar.service.SpeakerService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("editor/event")
@PreAuthorize("hasRole('SUPERUSER')")
public class EventResourceController {

    private static final String HTML_FOLDER = "editor/event/";

    private final SpeakerRepository speakerRepository;
    private final EventService eventService;

    @Autowired
    public EventResourceController(
            SpeakerRepository speakerRepository,
            EventService eventService
    ) {
        this.speakerRepository = speakerRepository;
        this.eventService = eventService;
    }

    @GetMapping("")
    public String index(
            @RequestParam(required = false) Integer shiftId,
            Model model
    ) {

        model.addAttribute("eventModels", eventService.getEventModelsByShiftId(shiftId));

        return HTML_FOLDER + "index";
    }

    @GetMapping("add")
    public String add(
            @PathVariable int shiftId,
            @PathVariable int activityId,
            Model model
    ) {

        model.addAttribute(new EventForm());
        model.addAttribute("speakers", speakerRepository.getAllByActivityIdOrderByCreatedAt(activityId));

        return HTML_FOLDER + "add";
    }

    @PostMapping("add")
    public String add(
            @PathVariable int shiftId,
            @Valid EventForm eventForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
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
            BindingResult bindingResult
    ) {

        eventForm.setId(eventId);
        if (bindingResult.hasErrors()) {
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
