package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.TeamRole;
import ru.forumcalendar.forumcalendar.repository.TeamRoleRepository;
import ru.forumcalendar.forumcalendar.service.TeamService;

import java.util.List;

@Service
public class BaseTeamService implements TeamService {

    private TeamRoleRepository teamRoleRepository;

    @Autowired
    public BaseTeamService(TeamRoleRepository teamRoleRepository) {
        this.teamRoleRepository = teamRoleRepository;
    }

    @Override
    public List<TeamRole> getAllRoles() {
        return teamRoleRepository.findAll();
    }
}
