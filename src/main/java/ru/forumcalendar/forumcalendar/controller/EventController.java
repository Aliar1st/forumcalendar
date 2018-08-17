package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.model.EventModel;
import ru.forumcalendar.forumcalendar.model.form.EventForm;
import ru.forumcalendar.forumcalendar.service.EventService;

import javax.validation.Valid;

@Controller
@RequestMapping("/event")
public class EventController {

    private static final String HTML_FOLDER = "event";
    private final EventService eventService;
    private final ConversionService conversionService;

    @Autowired
    public EventController(
            EventService eventService,
            @Qualifier("mvcConversionService") ConversionService conversionService
    ) {
        this.eventService = eventService;
        this.conversionService = conversionService;
    }

    @GetMapping("{id}")
    public String show(
            @PathVariable int id,
            Model model
    ) {
        model.addAttribute("event", conversionService.convert(eventService.get(id), EventModel.class));

        return HTML_FOLDER + "show";
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
        eventForm.setId(id);
        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "edit";
        }

        eventService.save(eventForm);

        return "redirect:..";
    }
}
