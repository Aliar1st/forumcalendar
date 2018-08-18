package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.forumcalendar.forumcalendar.domain.TeamRole;
import ru.forumcalendar.forumcalendar.domain.UserTeam;
import ru.forumcalendar.forumcalendar.repository.TeamRoleRepository;
import ru.forumcalendar.forumcalendar.repository.UserTeamRepository;
import ru.forumcalendar.forumcalendar.service.TeamRoleService;

import java.util.List;

@Service
@Transactional
public class BaseTeamRoleService implements TeamRoleService {

    private final UserTeamRepository userTeamRepository;
    private final TeamRoleRepository teamRoleRepository;

    @Autowired
    public BaseTeamRoleService(
            UserTeamRepository userTeamRepository,
            TeamRoleRepository teamRoleRepository
    ) {
        this.userTeamRepository = userTeamRepository;
        this.teamRoleRepository = teamRoleRepository;
    }

    @Override
    public boolean isUserCuratorOfTeam(String userId, int teamId) {
        UserTeam userTeam = userTeamRepository.getByUserIdAndTeamId(userId, teamId);
        return userTeam != null && userTeam.getTeamRole().getId() == TeamRole.ROLE_CURATOR_ID;
    }

    @Override
    public boolean isUserCaptainOfTeam(String userId, int teamId) {
        UserTeam userTeam = userTeamRepository.getByUserIdAndTeamId(userId, teamId);
        return userTeam != null && userTeam.getTeamRole().getId() == TeamRole.ROLE_CAPTAIN_ID;
    }

    @Override
    public boolean isUserMemberOfTeam(String userId, int teamId) {
        UserTeam userTeam = userTeamRepository.getByUserIdAndTeamId(userId, teamId);
        return userTeam != null;
    }

    @Override
    public boolean exist(int id) {
        return teamRoleRepository.findById(id).isPresent();
    }

    @Override
    public List<TeamRole> getAllRoles() {
        return teamRoleRepository.findAll();
    }
}
