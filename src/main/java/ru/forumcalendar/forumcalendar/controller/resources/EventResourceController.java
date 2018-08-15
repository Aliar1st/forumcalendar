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
import ru.forumcalendar.forumcalendar.model.EventModel;
import ru.forumcalendar.forumcalendar.model.form.EventForm;
import ru.forumcalendar.forumcalendar.repository.SpeakerRepository;
import ru.forumcalendar.forumcalendar.service.EventService;
import ru.forumcalendar.forumcalendar.service.SpeakerService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("editor/activity/{activityId}/shift/{shiftId}/event")
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

    @PreAuthorize("@baseShiftService.hasPermissionToWrite(#shiftId) or hasRole('SUPERUSER')")
    @GetMapping("")
    public String index(
            @P("shiftId") @PathVariable int shiftId,
            Model model
    ) {

        model.addAttribute("eventModels", eventService.getEventModelsByShiftId(shiftId));

        return HTML_FOLDER + "index";
    }

    @PreAuthorize("(@baseShiftService.hasPermissionToWrite(#shiftId) and @baseActivityService.hasPermissionToWrite(#activityId)) or hasRole('SUPERUSER')")
    @GetMapping("add")
    public String add(
            @P("shiftId") @PathVariable int shiftId,
            @P("activityId") @PathVariable int activityId,
            Model model
    ) {

        model.addAttribute(new EventForm());
        model.addAttribute("speakers", speakerRepository.getAllByActivityIdOrderByCreatedAt(activityId));

        return HTML_FOLDER + "add";
    }

    @PreAuthorize("@baseShiftService.hasPermissionToWrite(#shiftId) or hasRole('SUPERUSER')")
    @PostMapping("add")
    public String add(
            @P("shiftId") @PathVariable int shiftId,
            @Valid EventForm eventForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "add";
        }

        eventService.save(eventForm);

        return "redirect:";
    }

    @PreAuthorize("@baseEventService.hasPermissionToWrite(#eventId) or hasRole('SUPERUSER')")
    @GetMapping("{eventId}/edit")
    public String edit(
            @P("eventId") @PathVariable int eventId,
            Model model
    ) {

        EventForm eventForm = new EventForm(eventService.get(eventId));
        model.addAttribute(eventForm);

        return HTML_FOLDER + "edit";
    }

    @PreAuthorize("@baseEventService.hasPermissionToWrite(#eventId) or hasRole('SUPERUSER')")
    @PostMapping("{eventId}/edit")
    public String edit(
            @P("eventId") @PathVariable int eventId,
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

    @PreAuthorize("@baseEventService.hasPermissionToWrite(#eventId) or hasRole('SUPERUSER')")
    @GetMapping("{eventId}/delete")
    public String delete(
            @P("eventId") @PathVariable int eventId
    ) {

        eventService.delete(eventId);

        return "redirect:..";
    }
}
