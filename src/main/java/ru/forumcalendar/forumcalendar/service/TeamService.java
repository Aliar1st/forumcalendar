package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Team;
import ru.forumcalendar.forumcalendar.domain.TeamRole;
import ru.forumcalendar.forumcalendar.domain.UserTeam;
import ru.forumcalendar.forumcalendar.model.TeamModel;
import ru.forumcalendar.forumcalendar.model.form.TeamForm;

import java.util.List;
import java.util.Map;

public interface TeamService {

    boolean exist(int id);

    Team get(int id);

    Team save(TeamForm teamForm);

    void delete(int id);

    UserTeam joinCurrentUserToTeam(int teamId, int teamRoleId);

    List<TeamModel> getTeamModelsByShiftId(int id);

    List<TeamModel> getTeamModelsWithoutCuratorByShiftId(int id);

    Map<Integer, String> getTeamIdNameMapByShiftId(int id);

//    boolean isUserTeam(int id);
}
