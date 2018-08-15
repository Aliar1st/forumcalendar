package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.TeamRole;
import ru.forumcalendar.forumcalendar.repository.TeamRoleRepository;
import ru.forumcalendar.forumcalendar.service.TeamRoleService;
import ru.forumcalendar.forumcalendar.validation.annotation.ActivityExist;

import java.util.List;

@Service
public class BaseTeamRoleService implements TeamRoleService {

    private final TeamRoleRepository teamRoleRepository;

    @Autowired
    public BaseTeamRoleService(TeamRoleRepository teamRoleRepository) {
        this.teamRoleRepository = teamRoleRepository;
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
