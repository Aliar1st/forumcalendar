package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Team;
import ru.forumcalendar.forumcalendar.domain.TeamRole;
import ru.forumcalendar.forumcalendar.model.TeamModel;
import ru.forumcalendar.forumcalendar.model.form.TeamForm;

import java.util.List;

public interface TeamService {

    boolean exist(int id);

    Team get(int id);

    Team save(TeamForm teamForm);

    void delete(int id);

    List<TeamModel> getTeamModelsByShiftId(int id);

    List<TeamRole> getAllRoles();
}
