package ru.forumcalendar.forumcalendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.forumcalendar.forumcalendar.service.SubscriptionService;

import javax.jws.WebParam;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/sub")
    @ResponseBody
    public Map<String, Object> toggleSub() {

        subscriptionService.toggleSubscribe(5);

        Map<String, Object> map = new HashMap<>();

        map.put("status", "ok");
        map.put("subcount", subscriptionService.getEventModelsBySubscription().size());

        return map;
    }


    @GetMapping("/subs")
    public String subs(Model model) {

        model.addAttribute("eventModels", subscriptionService.getEventModelsBySubscription());

        return "event/subsTest";
    }
}
