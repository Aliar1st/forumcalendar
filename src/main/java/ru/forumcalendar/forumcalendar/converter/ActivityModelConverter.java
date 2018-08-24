package ru.forumcalendar.forumcalendar.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.domain.Shift;
import ru.forumcalendar.forumcalendar.model.ActivityModel;
import ru.forumcalendar.forumcalendar.model.TeamModel;
import ru.forumcalendar.forumcalendar.service.EventService;
import ru.forumcalendar.forumcalendar.service.SpeakerService;
import ru.forumcalendar.forumcalendar.service.TeamService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ActivityModelConverter implements Converter<Activity, ActivityModel> {

    private TeamService teamService;
    private EventService eventService;
    private SpeakerService speakerService;

    @Override
    public ActivityModel convert(Activity activity) {

        ActivityModel activityModel = new ActivityModel();
        activityModel.setId(activity.getId());
        activityModel.setName(activity.getName());
        activityModel.setPlace(activity.getPlace());
        activityModel.setDescription(activity.getDescription());

        Set<Shift> shifts = activity.getShifts();
        Map<String, Integer> countsMap = getCounts(shifts);

        activityModel.setShiftCount(shifts.size());
        activityModel.setTeamCount(countsMap.get("teamCount"));
        activityModel.setEventCount(countsMap.get("eventCount"));
        activityModel.setMemberCount(countsMap.get("memberCount"));
        activityModel.setSpeakerCount(speakerService.getSpeakerModelsByActivityId(activity.getId()).size());

        if (!shifts.isEmpty()) {
            Shift shiftTmp = shifts.iterator().next();
            LocalDate start_date = shiftTmp.getStartDate();
            LocalDate end_date = shiftTmp.getEndDate();

            for (Shift shift : shifts) {
                if (start_date.isAfter(shift.getStartDate()))
                    start_date = shift.getStartDate();

                if (end_date.isBefore(shift.getEndDate()))
                    end_date = shift.getEndDate();
            }

            activityModel.setStartDate(start_date);
            activityModel.setEndDate(end_date);
        }

        return activityModel;
    }

    private Map<String, Integer> getCounts(Set<Shift> shifts) {
        Map<String, Integer> map = new HashMap<>();
        int teamCount = 0;
        int eventCount = 0;
        int memberCount = 0;
        for (Shift shift : shifts) {
            List<TeamModel> teamModels = teamService.getTeamModelsByShiftId(shift.getId());
            teamCount += teamModels.size();
            eventCount += eventService.getEventModelsByShiftId(shift.getId()).size();
            for (TeamModel team : teamModels) {
                memberCount += team.getUserCount();
            }
        }
        map.put("eventCount", eventCount);
        map.put("teamCount", teamCount);
        map.put("memberCount", memberCount);
        return map;
    }


    @Autowired
    public void setTeamService(@Lazy TeamService teamService) {
        this.teamService = teamService;
    }

    @Autowired
    public void setEventService(@Lazy EventService eventService) {
        this.eventService = eventService;
    }

    @Autowired
    public void setSpeakerService(@Lazy SpeakerService speakerService) {
        this.speakerService = speakerService;
    }
}
