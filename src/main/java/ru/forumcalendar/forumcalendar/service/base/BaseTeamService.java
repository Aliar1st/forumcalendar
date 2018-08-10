package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.Team;
import ru.forumcalendar.forumcalendar.domain.TeamRole;
import ru.forumcalendar.forumcalendar.exception.EntityNotFoundException;
import ru.forumcalendar.forumcalendar.model.TeamModel;
import ru.forumcalendar.forumcalendar.model.form.TeamForm;
import ru.forumcalendar.forumcalendar.repository.ShiftRepository;
import ru.forumcalendar.forumcalendar.repository.TeamRepository;
import ru.forumcalendar.forumcalendar.repository.TeamRoleRepository;
import ru.forumcalendar.forumcalendar.service.TeamService;
import ru.forumcalendar.forumcalendar.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaseTeamService implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamRoleRepository teamRoleRepository;
    private final ShiftRepository shiftRepository;

    private final UserService userService;
    private final ConversionService conversionService;

    @Autowired
    public BaseTeamService(
            TeamRepository teamRepository,
            TeamRoleRepository teamRoleRepository,
            ShiftRepository shiftRepository,
            UserService userService,
            @Qualifier("mvcConversionService") ConversionService conversionService
    ) {
        this.teamRepository = teamRepository;
        this.teamRoleRepository = teamRoleRepository;
        this.shiftRepository = shiftRepository;
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @Override
    public boolean exist(int id) {
        return teamRepository.findById(id).isPresent();
    }

    @Override
    public Team get(int id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team with id " + id + " not found"));
    }

    @Override
    public Team save(TeamForm teamForm) {

        Team team = teamRepository.findById(teamForm.getId()).orElse(new Team());
        team.setUser(userService.getCurrentUser());
        team.setName(teamForm.getName());
        team.setDirection(teamForm.getDirection());
        team.setDescription(teamForm.getDescription());
        team.setShift(shiftRepository.findById(teamForm.getShiftId())
                .orElseThrow(() -> new EntityNotFoundException("Shift with id " + teamForm.getShiftId() + " not found")));

        return teamRepository.save(team);
    }

    @Override
    public void delete(int id) {
        teamRepository.deleteById(id);
    }

    @Override
    public List<TeamModel> getTeamModelsByShiftId(int id) {
        return teamRepository.getAllByShiftIdOrderByCreatedAt(id)
                .stream()
                .map((t) -> conversionService.convert(t, TeamModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamRole> getAllRoles() {
        return teamRoleRepository.findAll();
    }

    @Override
    public boolean isUserTeam(int id) {
        return get(id).getUser().getId().equals(userService.getCurrentId());
    }
}
