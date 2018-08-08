package ru.forumcalendar.forumcalendar.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import ru.forumcalendar.forumcalendar.domain.Team;
import ru.forumcalendar.forumcalendar.model.TeamModel;
import ru.forumcalendar.forumcalendar.repository.UserTeamRepository;

public class TeamModelConverter implements Converter<Team, TeamModel> {

    private UserTeamRepository userTeamRepository;

    @Override
    public TeamModel convert(Team team) {

        TeamModel teamModel = new TeamModel();

        teamModel.setId(team.getId());
        teamModel.setShiftId(team.getShift().getId());
        teamModel.setName(team.getName());
        teamModel.setDirection(team.getDirection());
        teamModel.setDescription(team.getDescription());
        teamModel.setUserCount(userTeamRepository.countAllByUserTeamIdentityTeamId(team.getId()));

        return teamModel;
    }

    @Autowired
    public void setUserTeamRepository(UserTeamRepository userTeamRepository) {
        this.userTeamRepository = userTeamRepository;
    }
}