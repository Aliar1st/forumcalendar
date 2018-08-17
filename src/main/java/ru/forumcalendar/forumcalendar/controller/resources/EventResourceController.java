package ru.forumcalendar.forumcalendar.controller.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.model.SpeakerModel;
import ru.forumcalendar.forumcalendar.model.form.EventForm;
import ru.forumcalendar.forumcalendar.service.EventService;
import ru.forumcalendar.forumcalendar.service.ShiftService;
import ru.forumcalendar.forumcalendar.service.SpeakerService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("editor/event")
@PreAuthorize("hasRole('ROLE_SUPERUSER')")
public class EventResourceController {

    private static final String HTML_FOLDER = "editor/event/";
    private static final String ROOT_MAPPING = "/editor/event?shiftId=";

    private final SpeakerService speakerService;
    private final EventService eventService;
    private final ShiftService shiftService;

    @Autowired
    public EventResourceController(
            SpeakerService speakerService,
            EventService eventService,
            ShiftService shiftService
    ) {
        this.speakerService = speakerService;
        this.eventService = eventService;
        this.shiftService = shiftService;
    }

    @GetMapping("")
    public String index(
            @RequestParam int shiftId,
            Model model
    ) {
        model.addAttribute("events", eventService.getEventModelsByShiftId(shiftId));
        model.addAttribute("shiftId", shiftId);

        return HTML_FOLDER + "index";
    }

    @GetMapping("add")
    public String add(
            @RequestParam int shiftId,
            Model model
    ) {
        EventForm eventForm = new EventForm();
        eventForm.setShiftId(shiftId);

        Activity activity = shiftService.get(shiftId).getActivity();
        List<SpeakerModel> speakers = speakerService.getSpeakerModelsByActivityId(activity.getId());

        model.addAttribute(eventForm);
        model.addAttribute("speakers", speakers);

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

        return "redirect:" + ROOT_MAPPING + eventForm.getShiftId();
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

        return "redirect:" + ROOT_MAPPING + eventForm.getShiftId();
    }

    @GetMapping("{id}/delete")
    public String delete(
            @PathVariable int id
    ) {
        Event event = eventService.delete(id);

        return "redirect:" + ROOT_MAPPING + event.getShift().getId();
    }
}
