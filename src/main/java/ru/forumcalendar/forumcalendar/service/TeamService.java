package ru.forumcalendar.forumcalendar.service;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.forumcalendar.forumcalendar.domain.Team;
import ru.forumcalendar.forumcalendar.domain.UserTeam;
import ru.forumcalendar.forumcalendar.model.TeamMemberModel;
import ru.forumcalendar.forumcalendar.model.TeamModel;
import ru.forumcalendar.forumcalendar.model.form.TeamForm;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface TeamService extends SecuredService, ResourceService<Team, TeamModel, TeamForm> {

    List<TeamModel>  getTeamModelsByActivityId(int activityId);

    void kickMember(String userId, int teamId);

    List<TeamModel> searchByName(String q, int shiftId) throws InterruptedException;

    Team updateTeamName(int teamId, String name);

    List<TeamMemberModel> getTeamMembers(int teamId);

    boolean becomeCaptainToggle(String userId, int teamId);

    UserTeam joinCurrentUserToTeam(int teamId, int teamRoleId);

    List<TeamModel> getTeamModelsByShiftId(int id);

    List<TeamModel> getTeamModelsWithoutCuratorByShiftId(int id);

    Map<Integer, String> getTeamIdNameMapByShiftId(int id);

    TeamMemberStatus getStatus(Integer teamId);

    String resolveTeamError(TeamMemberStatus teamMemberStatus, HttpSession httpSession, RedirectAttributes redirectAttributes);

//    boolean isUserTeam(int id);
}
