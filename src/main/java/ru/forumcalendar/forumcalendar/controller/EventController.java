package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.forumcalendar.forumcalendar.config.constt.SessionAttributeName;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.model.EventModel;
import ru.forumcalendar.forumcalendar.model.SpeakerModel;
import ru.forumcalendar.forumcalendar.model.form.EventForm;
import ru.forumcalendar.forumcalendar.service.EventService;
import ru.forumcalendar.forumcalendar.service.SpeakerService;
import ru.forumcalendar.forumcalendar.service.TeamEventService;
import ru.forumcalendar.forumcalendar.service.TeamService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    private static final String HTML_FOLDER = "event/";
    private static final String REDIRECT_ROOT_MAPPING = "redirect:/events";

    private final TeamService teamService;
    private final TeamEventService teamEventService;
    private final EventService eventService;
    private final SpeakerService speakerService;
    private final ConversionService conversionService;

    private final PermissionEvaluator permissionEvaluator;

    @Autowired
    public EventController(
            TeamService teamService,
            TeamEventService teamEventService,
            EventService eventService,
            SpeakerService speakerService,
            @Qualifier("mvcConversionService") ConversionService conversionService,
            PermissionEvaluator permissionEvaluator
    ) {
        this.teamService = teamService;
        this.teamEventService = teamEventService;
        this.eventService = eventService;
        this.speakerService = speakerService;
        this.conversionService = conversionService;
        this.permissionEvaluator = permissionEvaluator;
    }

    @GetMapping("day")
    public String indexDay(
            Model model,
            HttpSession httpSession,
            Authentication authentication
    ) {

        int teamId = (int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);
        Shift shift = teamService.get(teamId).getShift();

        if (!permissionEvaluator.hasPermission(authentication, shift.getId(), "Shift", "w")) {
            return REDIRECT_ROOT_MAPPING;
        }

        model.addAttribute("events", eventService.getEventModelsByShiftIdAndDate(shift.getId(), LocalDate.now()));
        model.addAttribute("teamEvents", teamEventService.getTeamEventModelsByTeamIdAndDate(teamId, LocalDate.now()));

        return HTML_FOLDER + "index";
    }

    @GetMapping("shift")
    public String indexShift(
            Model model,
            HttpSession httpSession,
            Authentication authentication
    ) {
        int teamId = (int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);
        Shift shift = teamService.get(teamId).getShift();

        if (!permissionEvaluator.hasPermission(authentication, shift.getId(), "Shift", "w")) {
            return REDIRECT_ROOT_MAPPING;
        }

        model.addAttribute("events", eventService.getEventModelsByShiftId(shift.getId()));
        model.addAttribute("teamEvents", teamEventService.getTeamEventModelsByTeamId(teamId));

        return HTML_FOLDER + "index";
    }

    @PreAuthorize("hasPermission(#id, 'Event', 'r')")
    @GetMapping("{id}")
    public String show(
            @PathVariable int id,
            Model model
    ) {
        model.addAttribute("event", conversionService.convert(eventService.get(id), EventModel.class));

        return HTML_FOLDER + "show";
    }

    @GetMapping("add")
    public String add(
            Model model,
            HttpSession httpSession,
            Authentication authentication
    ) {
        int teamId = (int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);
        Shift shift = teamService.get(teamId).getShift();

        if (!permissionEvaluator.hasPermission(authentication, shift.getId(), "Shift", "w")) {
            return REDIRECT_ROOT_MAPPING;
        }

        EventForm eventForm = new EventForm();
        eventForm.setShiftId(shift.getId());

        List<SpeakerModel> speakers = speakerService.getSpeakerModelsByActivityId(shift.getActivity().getId());

        model.addAttribute(eventForm);
        model.addAttribute("speakers", speakers);

        return HTML_FOLDER + "add";
    }

    @PostMapping("add")
    public String add(
            @Valid EventForm eventForm,
            BindingResult bindingResult,
            Authentication authentication
    ) {
        if (!permissionEvaluator.hasPermission(authentication, eventForm.getShiftId(), "Shift", "w")) {
            return REDIRECT_ROOT_MAPPING;
        }

        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "add";
        }

        eventService.save(eventForm);

        return REDIRECT_ROOT_MAPPING;
    }

    @PreAuthorize("hasPermission(#id, 'Event', 'w')")
    @GetMapping("{id}/edit")
    public String edit(
            @PathVariable int id,
            Model model
    ) {
        EventForm eventForm = new EventForm(eventService.get(id));
        model.addAttribute(eventForm);

        return HTML_FOLDER + "edit";
    }

    @PreAuthorize("hasPermission(#id, 'Event', 'w')")
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

        return REDIRECT_ROOT_MAPPING;
    }
}
