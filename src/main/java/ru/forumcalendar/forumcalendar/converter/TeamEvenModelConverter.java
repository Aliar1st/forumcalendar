package ru.forumcalendar.forumcalendar.converter;

import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.TeamEvent;
import ru.forumcalendar.forumcalendar.model.TeamEventModel;

public class TeamEvenModelConverter implements Converter<TeamEvent, TeamEventModel> {

    @Override
    public TeamEventModel convert(TeamEvent teamEvent) {

        TeamEventModel teamEventModel = new TeamEventModel();
        teamEventModel.setId(teamEvent.getId());
        teamEventModel.setName(teamEvent.getName());
        teamEventModel.setStartDatetime(teamEvent.getStartDatetime());
        teamEventModel.setEndDatetime(teamEvent.getEndDatetime());
        teamEventModel.setPlace(teamEvent.getPlace());
        teamEventModel.setDescription(teamEvent.getDescription());

        return teamEventModel;
    }
}
