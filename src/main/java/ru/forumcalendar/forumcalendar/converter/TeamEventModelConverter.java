package ru.forumcalendar.forumcalendar.converter;

import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.TeamEvent;
import ru.forumcalendar.forumcalendar.model.TeamEventModel;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TeamEventModelConverter implements Converter<TeamEvent, TeamEventModel> {

    @Override
    public TeamEventModel convert(TeamEvent teamEvent) {

        TeamEventModel teamEventModel = new TeamEventModel();
        teamEventModel.setId(teamEvent.getId());
        teamEventModel.setName(teamEvent.getName());
        teamEventModel.setStartDatetime(teamEvent.getStartDatetime());
        teamEventModel.setEndDatetime(teamEvent.getEndDatetime());
        teamEventModel.setPlace(teamEvent.getPlace());
        teamEventModel.setDescription(teamEvent.getDescription());

        LocalDateTime startEvent = teamEvent.getStartDatetime();
        LocalDateTime startShift = teamEvent.getTeam().getShift().getStartDate().atStartOfDay();

        teamEventModel.setDay((int) ChronoUnit.DAYS.between(startShift, startEvent));

        return teamEventModel;
    }
}
