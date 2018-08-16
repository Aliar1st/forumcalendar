package ru.forumcalendar.forumcalendar.controller;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.forumcalendar.forumcalendar.config.constt.SessionAttributeName;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.domain.Team;
import ru.forumcalendar.forumcalendar.model.form.SubscriptionForm;
import ru.forumcalendar.forumcalendar.service.EventService;
import ru.forumcalendar.forumcalendar.service.SubscriptionService;
import ru.forumcalendar.forumcalendar.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SubscriptionController {

    private static final String HTML_FOLDER = "subscription/";

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final EventService eventService;

    private final SimpMessagingTemplate template;

    @Autowired
    public SubscriptionController(
            UserService userService,
            SubscriptionService subscriptionService,
            EventService eventService,
            SimpMessagingTemplate template
    ) {
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
            Model model
    ) {

        Cookie cookie = new Cookie(
                "uniqueId",
                userService.getCurrentUser(principal).getId()
        );
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        Team team = (Team) httpSession.getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);

        model.addAttribute("events", subscriptionService.getEventModelsBySubscription(team.getShift().getId()));

        return HTML_FOLDER + "favorites";
    }

    // TODO: 8/16/2018 Сделать возможность отписки и подписки


//    @PostMapping("/toggle-subscribe")
//    @ResponseBody
//    public Map<String, Object> toggleSubscribe(
//            @Valid SubscriptionForm subscriptionForm,
//            BindingResult bindingResult
//    ) {
//        if (bindingResult.hasErrors()) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("status", "error");
//            map.put("cause", bindingResult.getAllErrors());
//            return map;
//        }
//
//        subscriptionService.toggleSubscribe(subscriptionForm.getEventId());
//
//        return Collections.singletonMap("status", "ok");
//    }

//    @GetMapping("/subs")
//    public String subs(
//            Model model,
//            Principal principal,
//            HttpServletResponse response
//    ) {
//
//        Cookie cookie = new Cookie(
//                "uniqueId",
//                userService.getCurrentUser(principal).getId()
//        );
//        cookie.setMaxAge(3600);
//        response.addCookie(cookie);
//
//        model.addAttribute("eventModels", subscriptionService.getEventModelsBySubscription());
//
//        return "event/subsTest";
//    }

    @MessageMapping("/sub/{uniqueId}")
    @SendTo("/successSub/{uniqueId}")
    public String toggleSub(
            int eventId,
            Principal principal,
            @DestinationVariable String uniqueId
    ) throws SchedulerException {

        boolean sub;

        Event event = eventService.get(eventId);

        sub = subscriptionService.toggleSubscribe(
                eventId,
                userService.getCurrentUser(principal).getId(),
                () -> template.convertAndSend("/notify/" + uniqueId, event)
        );

        if (sub) {
            return "Вы подписались на событие " + event.getName();
        } else {
            return "Вы отписались от события " + event.getName();
        }
    }
}
