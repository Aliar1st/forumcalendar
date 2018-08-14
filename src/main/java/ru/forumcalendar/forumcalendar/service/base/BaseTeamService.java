package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.Team;
import ru.forumcalendar.forumcalendar.domain.TeamRole;
import ru.forumcalendar.forumcalendar.domain.UserTeam;
import ru.forumcalendar.forumcalendar.domain.UserTeamIdentity;
import ru.forumcalendar.forumcalendar.exception.EntityNotFoundException;
import ru.forumcalendar.forumcalendar.model.TeamModel;
import ru.forumcalendar.forumcalendar.model.form.TeamForm;
import ru.forumcalendar.forumcalendar.repository.ShiftRepository;
import ru.forumcalendar.forumcalendar.repository.TeamRepository;
import ru.forumcalendar.forumcalendar.repository.TeamRoleRepository;
import ru.forumcalendar.forumcalendar.repository.UserTeamRepository;
import ru.forumcalendar.forumcalendar.service.TeamService;
import ru.forumcalendar.forumcalendar.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BaseTeamService implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamRoleRepository teamRoleRepository;
    private final ShiftRepository shiftRepository;
    private final UserTeamRepository userTeamRepository;

    private final UserService userService;
    private final ConversionService conversionService;

    @Autowired
    public BaseTeamService(
            TeamRepository teamRepository,
            TeamRoleRepository teamRoleRepository,
            ShiftRepository shiftRepository,
            UserTeamRepository userTeamRepository,
            UserService userService,
            @Qualifier("mvcConversionService") ConversionService conversionService
    ) {
        this.teamRepository = teamRepository;
        this.teamRoleRepository = teamRoleRepository;
        this.shiftRepository = shiftRepository;
        this.userTeamRepository = userTeamRepository;
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
//        team.setUser(userService.getCurrentUser());
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
    public UserTeam joinCurrentUserToTeam(int teamId, int teamRoleId) {

        UserTeam userTeam = new UserTeam();

        UserTeamIdentity userTeamIdentity = new UserTeamIdentity();
        userTeamIdentity.setTeam(get(teamId));
        userTeamIdentity.setUser(userService.getCurrentUser());

        userTeam.setUserTeamIdentity(userTeamIdentity);
        userTeam.setTeamRole(teamRoleRepository.findById(teamRoleId)
                .orElseThrow(() -> new EntityNotFoundException("Team role with id " + teamRoleId + " not found")));

        return userTeamRepository.save(userTeam);
    }

    @Override
    public List<TeamModel> getTeamModelsByShiftId(int id) {
        return teamRepository.getAllByShiftIdOrderByCreatedAt(id)
                .stream()
                .map((t) -> conversionService.convert(t, TeamModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamModel> getTeamModelsWithoutCuratorByShiftId(int id) {
        return teamRepository.getAllByShiftIdAndWithoutCuratorOrderByCreatedAt(id)
                .stream()
                .map((t) -> conversionService.convert(t, TeamModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, String> getTeamIdNameMapByShiftId(int id) {
        return teamRepository.getAllByShiftIdOrderByCreatedAt(id)
                .stream()
                .collect(Collectors.toMap(Team::getId, Team::getName));
    }

    @Override
    public boolean hasPermissionToWrite(int id) {
        return true;
    }

    @Override
    public boolean hasPermissionToRead(int id) {
        return false;
    }

//    @Override
//    public boolean isUserTeam(int id) {
//        return get(id).getUser().getId().equals(userService.getCurrentId());
//    }
}
