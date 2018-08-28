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
import ru.forumcalendar.forumcalendar.model.form.AddEventDateForm;
import ru.forumcalendar.forumcalendar.model.form.ChoosingEventsDate;
import ru.forumcalendar.forumcalendar.model.form.EventForm;
import ru.forumcalendar.forumcalendar.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/events")
public class EventController {

    private static final String HTML_FOLDER = "event/";
    private static final String REDIRECT_ROOT_MAPPING = "redirect:/events";
    private static final Map<String, String> REDIRECT_MAP = new HashMap<>();

    static {
        REDIRECT_MAP.put("default", REDIRECT_ROOT_MAPPING + "/choosing_date");
        REDIRECT_MAP.put("calendar", REDIRECT_ROOT_MAPPING + "?date=");
        REDIRECT_MAP.put("shift", REDIRECT_ROOT_MAPPING + "/shiftIndex?shiftId=");
        REDIRECT_MAP.put("activity", REDIRECT_ROOT_MAPPING + "/activityIndex?activityId=");
    }

    private final TeamService teamService;
    private final TeamEventService teamEventService;
    private final EventService eventService;
    private final SpeakerService speakerService;
    private final ShiftService shiftService;
    private final ConversionService conversionService;

    private final PermissionEvaluator permissionEvaluator;

    @Autowired
    public EventController(
            TeamService teamService,
            TeamEventService teamEventService,
            EventService eventService,
            SpeakerService speakerService,
            ShiftService shiftService,
            @Qualifier("mvcConversionService") ConversionService conversionService,
            PermissionEvaluator permissionEvaluator
    ) {
        this.teamService = teamService;
        this.teamEventService = teamEventService;
        this.eventService = eventService;
        this.speakerService = speakerService;
        this.shiftService = shiftService;
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
            return REDIRECT_MAP.get("default");
        }
        model.addAttribute("events",events);
        model.addAttribute("day", choosingEventsDate.getDate().format(DateTimeFormatter.ofPattern("dd MMMM", Locale.forLanguageTag("ru"))));
        model.addAttribute("date", choosingEventsDate.getDate());

        return HTML_FOLDER + "index";
    }

    @GetMapping("/shiftIndex")
    public String shiftIndex(
            Model model,
            @RequestParam int shiftId
    ) {
        model.addAttribute("events", eventService.getEventModelsByShiftId(shiftId));
        model.addAttribute("shiftId", shiftId);
        return HTML_FOLDER + "shiftIndex";
    }

    @GetMapping("/activityIndex")
    public String activityIndex(
            Model model,
            @RequestParam int activityId
    ) {
        model.addAttribute("events", eventService.getEventModelsByActivityId(activityId));
        model.addAttribute("activityId", activityId);
        return HTML_FOLDER + "activityIndex";
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
            @Valid AddEventDateForm addEventDateForm,
            BindingResult bindingResult,
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
            if (!bindingResult.hasErrors()) {
                return REDIRECT_MAP.get("calendar")
                        + addEventDateForm.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            } else {
                return REDIRECT_MAP.get("default");
            }
        }

        EventForm eventForm = new EventForm();
        eventForm.setShiftId(shift.getId());

        if (!bindingResult.hasErrors()) {
            eventForm.setStartDatetime(addEventDateForm.getDate().atTime(0, 0));
            eventForm.setEndDatetime(addEventDateForm.getDate().atTime(0, 0));
        }

        List<SpeakerModel> speakers = speakerService.getSpeakerModelsByActivityId(shift.getActivity().getId());

        model.addAttribute(eventForm);
        model.addAttribute("speakers", speakers);
        model.addAttribute("shifts", shiftService.getShiftModelsByActivityId(shift.getActivity().getId()));
        model.addAttribute("redirectType", "calendar");

        return HTML_FOLDER + "add";
    }

    @GetMapping("add/shift")
    public String shiftAdd(
            Model model,
            Authentication authentication,
            @RequestParam int shiftId
    ) {
        if (!permissionEvaluator.hasPermission(authentication, shiftId, "Shift", "w")) {
            return REDIRECT_ROOT_MAPPING;
        }

        EventForm eventForm = new EventForm();
        eventForm.setShiftId(shiftId);

        List<SpeakerModel> speakers = speakerService.getSpeakerModelsByShiftId(shiftId);

        model.addAttribute(eventForm);
        model.addAttribute("speakers", speakers);
        model.addAttribute("shifts", shiftService.getShiftModelsByActivityId(shiftService.get(shiftId).getActivity().getId()));
        model.addAttribute("shiftId", shiftId);
        model.addAttribute("redirectType", "shift");

        return HTML_FOLDER + "add";
    }

    @GetMapping("add/activity")
    public String activityAdd(
            Model model,
            Authentication authentication,
            @RequestParam int activityId
    ) {
        if (!permissionEvaluator.hasPermission(authentication, activityId, "Activity", "w")) {
            return REDIRECT_ROOT_MAPPING;
        }

        EventForm eventForm = new EventForm();

        List<SpeakerModel> speakers = speakerService.getSpeakerModelsByActivityId(activityId);

        model.addAttribute(eventForm);
        model.addAttribute("speakers", speakers);
        model.addAttribute("shifts", shiftService.getShiftModelsByActivityId(activityId));
        model.addAttribute("redirectType", "activity");

        return HTML_FOLDER + "add";
    }

    @PostMapping("add")
    public String add(
            Model model,
            String redirectType,
            @Valid EventForm eventForm,
            BindingResult bindingResult,
            Authentication authentication
    ) {
        Integer shiftId = null;

        if (eventForm.getShiftId() != null)
            shiftId = eventForm.getShiftId();
        else if (eventForm.getShiftsId() != null
                && !eventForm.getShiftsId().isEmpty()) {
            shiftId = eventForm.getShiftsId().get(0);
        }

        if (eventForm.getShiftId() != null && !permissionEvaluator.hasPermission(authentication, shiftId, "Shift", "w")) {
            return REDIRECT_ROOT_MAPPING;
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("shifts", shiftService.getShiftModelsByActivityId(shiftService.get(eventForm.getShiftId()).getActivity().getId()));
            return HTML_FOLDER + "add";
        }

        Event event = eventService.save(eventForm);

        if (REDIRECT_MAP.containsKey(redirectType)) {
            switch (redirectType) {
                case "calendar":
                    return REDIRECT_MAP.get("calendar")
                            + event.getStartDatetime().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                case "shift":
                    return REDIRECT_MAP.get("shift")
                            + event.getShift().getId();
                case "activity":
                    return REDIRECT_MAP.get("activity")
                            + event.getShift().getActivity().getId();
                default:
                    return REDIRECT_MAP.get("default");
            }
        } else {
            return REDIRECT_MAP.get("default");
        }
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

        return REDIRECT_ROOT_MAPPING + "/shiftIndex?shiftId=" + eventForm.getShiftId();
    }

    @PreAuthorize("hasPermission(#id, 'Event', 'w')")
    @GetMapping("{id}/delete")
    public String delete(
            @PathVariable int id,
            HttpServletRequest request
    ) {
        eventService.delete(id);

        return "redirect:" + request.getHeader("referer");
    }
}
