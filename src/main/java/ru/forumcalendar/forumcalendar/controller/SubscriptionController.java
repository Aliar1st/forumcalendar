package ru.forumcalendar.forumcalendar.controller;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.forumcalendar.forumcalendar.config.constt.SessionAttributeName;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.domain.Team;
import ru.forumcalendar.forumcalendar.model.ShiftEventModel;
import ru.forumcalendar.forumcalendar.model.form.ToggleSubscribeForm;
import ru.forumcalendar.forumcalendar.service.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SubscriptionController {

    private static final String HTML_FOLDER = "subscription/";

    private final TeamService teamService;
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final EventService eventService;

    private final SimpMessagingTemplate template;

    @Autowired
    public SubscriptionController(
            TeamService teamService,
            UserService userService,
            SubscriptionService subscriptionService,
            EventService eventService,
            SimpMessagingTemplate template
    ) {
        this.teamService = teamService;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.eventService = eventService;
        this.template = template;
    }

    @GetMapping("/favorites")
    public String favorites(
            HttpSession httpSession,
            Principal principal,
            HttpServletResponse response,
            Model model,
            RedirectAttributes redirectAttributes
    ) throws SchedulerException {

        Cookie cookie = new Cookie(
                "uniqueId",
                userService.getCurrentUser(principal).getId()
        );
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        int teamId = (int) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);

        TeamMemberStatus teamMemberStatus = teamService.getStatus(teamId);
        if (TeamMemberStatus.OK != teamMemberStatus) {
            return teamService.resolveTeamError(teamMemberStatus, httpSession, redirectAttributes);
        }

        Team team = teamService.get(teamId);

        List<ShiftEventModel> f = subscriptionService.getEventModelsBySubscription(team.getShift().getId());

        model.addAttribute("events", subscriptionService.getEventModelsBySubscription(team.getShift().getId()));

        return HTML_FOLDER + "favorites";
    }

    @ResponseBody
    @PostMapping("/sub")
    public Map<String, Object> toggleSubscribe(
            @Valid ToggleSubscribeForm form,
            BindingResult bindingResult,
            Principal principal
    ) throws SchedulerException {

        boolean sub;
        Map<String, Object> map = new HashMap<>();

        if (bindingResult.hasErrors()) {
            map.put("status", "error");
            map.put("cause", bindingResult.getAllErrors());
            return map;
        }

        Event event = eventService.get(form.getEventId());

        sub = subscriptionService.toggleSubscribe(
                form.getEventId(),
                userService.getCurrentUser(principal).getId(),
                () -> template.convertAndSend("/notify/" + form.getUniqueId(), event)
        );

        map.put("eventName", event.getName());
        map.put("isSubscribe", sub);
        map.put("status", "ok");

        return map;
    }

//    @MessageMapping("/sub/{uniqueId}")
//    @SendTo("/successSub/{uniqueId}")
//    public String toggleSub(
//            int eventId,
//            Principal principal,
//            @DestinationVariable String uniqueId
//    ) throws SchedulerException {
//
//        boolean sub;
//
//        Event event = eventService.get(eventId);
//
//        sub = subscriptionService.toggleSubscribe(
//                eventId,
//                userService.getCurrentUser(principal).getId(),
//                () -> template.convertAndSend("/notify/" + uniqueId, event)
//        );
//
//        if (sub) {
//            return "Вы подписались на событие " + event.getName();
//        } else {
//            return "Вы отписались от события " + event.getName();
//        }
//    }
}
