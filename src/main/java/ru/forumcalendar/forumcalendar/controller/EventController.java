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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.forumcalendar.forumcalendar.config.constt.SessionAttributeName;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.model.*;
import ru.forumcalendar.forumcalendar.model.ShiftEventModel;
import ru.forumcalendar.forumcalendar.model.TeamEventModel;
import ru.forumcalendar.forumcalendar.model.form.ChoosingEventsDate;
import ru.forumcalendar.forumcalendar.model.form.EventForm;
import ru.forumcalendar.forumcalendar.service.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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

    @GetMapping("choosing_date")
    public String choosingDate(
        Model model
    ) {
        model.addAttribute("now", LocalDate.now());
        return HTML_FOLDER + "choosing_date";
    }

//    @PostMapping("choosing_date")
//    public String choosingDate(
//            @Valid ChoosingEventsDate choosingEventsDate,
//            BindingResult bindingResult,
//            HttpSession httpSession,
//            RedirectAttributes redirectAttributes
//    ) {
//        if (bindingResult.hasErrors()) {
//            return HTML_FOLDER + "choosing_date";
//        }
//
//        int teamId = (int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);
//
//        TeamMemberStatus teamMemberStatus = teamService.getStatus(teamId);
//        if (TeamMemberStatus.OK != teamMemberStatus) {
//            return teamService.resolveTeamError(teamMemberStatus, httpSession, redirectAttributes);
//        }
//
//        int shiftId = teamService.get(teamId).getShift().getId();
//
//        List<ShiftEventModel> shiftEventModels = eventService.getEventModelsByShiftIdAndDate(shiftId, choosingEventsDate.getDate());
//        List<TeamEventModel> teamEventModels = teamEventService.getTeamEventModelsByTeamIdAndDate(teamId, choosingEventsDate.getDate());
//
//        List<EventModel> events = new ArrayList<>(shiftEventModels.size() + teamEventModels.size());
//        events.addAll(shiftEventModels);
//        events.addAll(teamEventModels);
//        Collections.sort(events);
//
//        if (events.isEmpty()) {
//            redirectAttributes.addFlashAttribute("error", "Events on this date not found");
//            return REDIRECT_ROOT_MAPPING + "/choosing_date";
//        }
//
//        redirectAttributes.addFlashAttribute("events", events);
//
//        return REDIRECT_ROOT_MAPPING;
//    }

    @GetMapping("")
    public String index(
            Model model,
            @Valid ChoosingEventsDate choosingEventsDate,
            BindingResult bindingResult,
            HttpSession httpSession,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return HTML_FOLDER + "choosing_date";
        }

        int teamId = (int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);

        TeamMemberStatus teamMemberStatus = teamService.getStatus(teamId);
        if (TeamMemberStatus.OK != teamMemberStatus) {
            return teamService.resolveTeamError(teamMemberStatus, httpSession, redirectAttributes);
        }

        int shiftId = teamService.get(teamId).getShift().getId();

        List<ShiftEventModel> shiftEventModels = eventService.getEventModelsByShiftIdAndDate(shiftId, choosingEventsDate.getDate());
        List<TeamEventModel> teamEventModels = teamEventService.getTeamEventModelsByTeamIdAndDate(teamId, choosingEventsDate.getDate());

        List<EventModel> events = new ArrayList<>(shiftEventModels.size() + teamEventModels.size());
        events.addAll(shiftEventModels);
        events.addAll(teamEventModels);
        Collections.sort(events);

        if (events.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Events on this date not found");
            return REDIRECT_ROOT_MAPPING + "/choosing_date";
        }

        model.addAttribute("events", events);
        model.addAttribute("day", choosingEventsDate.getDate().format(DateTimeFormatter.ofPattern("dd MMMM", Locale.forLanguageTag("ru"))));

        return HTML_FOLDER + "index";
    }

    @PreAuthorize("hasPermission(#id, 'Event', 'r')")
    @GetMapping("{id}")
    public String show(
            @PathVariable int id,
            Model model
    ) {
        model.addAttribute("event", conversionService.convert(eventService.get(id), ShiftEventModel.class));

        return HTML_FOLDER + "show";
    }

    @GetMapping("add")
    public String add(
            Model model,
            HttpSession httpSession,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        int teamId = (int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);

        TeamMemberStatus teamMemberStatus = teamService.getStatus(teamId);
        if (TeamMemberStatus.OK != teamMemberStatus) {
            return teamService.resolveTeamError(teamMemberStatus, httpSession, redirectAttributes);
        }

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
        Event event = eventService.get(id);
        EventForm eventForm = new EventForm(event);
        model.addAttribute(eventForm);
        model.addAttribute("speakers", speakerService.getSpeakerModelsByActivityId(event.getShift().getActivity().getId()));

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
