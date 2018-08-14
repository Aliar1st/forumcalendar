package ru.forumcalendar.forumcalendar.controller;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.service.EventService;
import ru.forumcalendar.forumcalendar.service.SubscriptionService;
import ru.forumcalendar.forumcalendar.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
public class SubscriptionController {

    private final UserService userService;
    private final SimpMessagingTemplate template;
    private final SubscriptionService subscriptionService;
    private final EventService eventService;

    @Autowired
    public SubscriptionController(
            UserService userService,
            SimpMessagingTemplate template,
            SubscriptionService subscriptionService,
            EventService eventService) throws SchedulerException {
        this.userService = userService;
        this.template = template;
        this.subscriptionService = subscriptionService;
        this.eventService = eventService;

    }

//    @PostMapping("/sub")
//    @ResponseBody
//    public Map<String, Object> toggleSub() {
//
//        subscriptionService.toggleSubscribe(1);
//
//        Map<String, Object> map = new HashMap<>();
//
//        map.put("status", "ok");
//        map.put("subcount", subscriptionService.getEventModelsBySubscription().size());
//
//        return map;
//    }


    @GetMapping("/subs")
    public String subs(
            Model model,
            Principal principal,
            HttpServletResponse response
    ) throws SchedulerException {

        Cookie cookie = new Cookie(
                "uniqueId",
                userService.getCurrentUser(principal).getId()
        );
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        model.addAttribute("eventModels", subscriptionService.getEventModelsBySubscription());

        return "event/subsTest";
    }

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
