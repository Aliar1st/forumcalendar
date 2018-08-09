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
import ru.forumcalendar.forumcalendar.service.EventService;

import javax.validation.Valid;

@Controller
@RequestMapping("editor/activity/{activityId}/shift/{shiftId}/event")
public class EventController {

    private static final String HTML_FOLDER = "editor/event/";

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
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
            Model model
    ) {

        model.addAttribute(new EventForm());

        return HTML_FOLDER + "add";
    }

    @PostMapping("add")
    public String add(
            @PathVariable int activityId,
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

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "edit";
        }

        eventForm.setId(eventId);
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
