package ru.forumcalendar.forumcalendar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import ru.forumcalendar.forumcalendar.service.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final Map<String, ResourceService> TYPE_SERVICE_MAP = new HashMap<>();

    private final static String TYPE_ACTIVITY = "ACTIVITY".toUpperCase();
    private final static String TYPE_SHIFT = "SHIFT".toUpperCase();
    private final static String TYPE_SPEAKER = "SPEAKER".toUpperCase();
    private final static String TYPE_TEAM = "TEAM".toUpperCase();
    private final static String TYPE_EVENT = "EVENT".toUpperCase();

//    private final ActivityService activityService;
//    private final ShiftService shiftService;
//    private final SpeakerService speakerService;
//    private final TeamService teamService;
//    private final EventService eventService;

    @Autowired
    public CustomPermissionEvaluator(
            ActivityService activityService,
            ShiftService shiftService,
            SpeakerService speakerService,
            TeamService teamService,
            EventService eventService
    ) {
//        this.activityService = activityService;
//        this.shiftService = shiftService;
//        this.speakerService = speakerService;
//        this.teamService = teamService;
//        this.eventService = eventService;

//        TYPE_SERVICE_MAP.put(TYPE_ACTIVITY, activityService);
//        TYPE_SERVICE_MAP.put(TYPE_SHIFT, activityService);
//        TYPE_SERVICE_MAP.put(TYPE_SPEAKER, activityService);
//        TYPE_SERVICE_MAP.put(TYPE_TEAM, activityService);
//        TYPE_SERVICE_MAP.put(TYPE_EVENT, activityService);
    }

    @Override
    public boolean hasPermission(
            Authentication auth,
            Object object,
            Object permission
    ) {
        if ((auth == null) || (object == null) || !(permission instanceof String)){
            return false;
        }

        return false;
    }

    @Override
    public boolean hasPermission(
            Authentication auth,
            Serializable id,
            String type,
            Object permission
    ) {
        if ((auth == null) || (id == null) || (type == null) || !(permission instanceof String)) {
            return false;
        }

        return false;
    }
}
