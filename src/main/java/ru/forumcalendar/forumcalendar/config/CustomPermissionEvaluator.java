package ru.forumcalendar.forumcalendar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import ru.forumcalendar.forumcalendar.service.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final Map<String, SecuredService> TYPE_SERVICE_MAP = new HashMap<>();

    private final static String TYPE_ACTIVITY = "ACTIVITY".toUpperCase();
    private final static String TYPE_SHIFT = "SHIFT".toUpperCase();
    private final static String TYPE_SPEAKER = "SPEAKER".toUpperCase();
    private final static String TYPE_TEAM = "TEAM".toUpperCase();
    private final static String TYPE_TEAM_EVENT = "TEAM_EVENT".toUpperCase();
    private final static String TYPE_EVENT = "EVENT".toUpperCase();

    private ActivityService activityService;
    private SpeakerService speakerService;
    private ShiftService shiftService;
    private EventService eventService;
    private TeamService teamService;
    private TeamEventService teamEventService;

    @Override
    public boolean hasPermission(
            Authentication auth,
            Object object,
            Object permission
    ) {
        if ((auth == null) || !(object instanceof String) || !(permission instanceof String)){
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

        String perm = (String) permission;
        String key = type.toUpperCase();

        if (TYPE_SERVICE_MAP.containsKey(key)) {
            SecuredService securedService = TYPE_SERVICE_MAP.get(key);
            Integer intId = (Integer) id;

            if (perm.equalsIgnoreCase("rw")) {
                return securedService.hasPermissionToRead(intId)
                    && securedService.hasPermissionToWrite(intId);
            } else if (perm.equalsIgnoreCase("r")) {
                return securedService.hasPermissionToRead(intId);
            } else if (perm.equalsIgnoreCase("w")) {
                return securedService.hasPermissionToWrite(intId);
            }
        }

        return false;
    }

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
        TYPE_SERVICE_MAP.put(TYPE_ACTIVITY, this.activityService);
    }

    @Autowired
    public void setShiftService(ShiftService shiftService) {
        this.shiftService = shiftService;
        TYPE_SERVICE_MAP.put(TYPE_SHIFT, this.shiftService);
    }

    @Autowired
    public void setSpeakerService(SpeakerService speakerService) {
        this.speakerService = speakerService;
        TYPE_SERVICE_MAP.put(TYPE_SPEAKER, this.speakerService);
    }

    @Autowired
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
        TYPE_SERVICE_MAP.put(TYPE_TEAM, this.teamService);
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
        TYPE_SERVICE_MAP.put(TYPE_EVENT, this.eventService);
    }

    @Autowired
    public void setTeamEventService(TeamEventService teamEventService) {
        this.teamEventService = teamEventService;
        TYPE_SERVICE_MAP.put(TYPE_TEAM_EVENT, this.teamEventService);
    }
}
