package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.forumcalendar.forumcalendar.domain.Team;
import ru.forumcalendar.forumcalendar.domain.TeamRole;
import ru.forumcalendar.forumcalendar.domain.UserTeam;
import ru.forumcalendar.forumcalendar.domain.UserTeamIdentity;
import ru.forumcalendar.forumcalendar.exception.EntityNotFoundException;
import ru.forumcalendar.forumcalendar.model.TeamMemberModel;
import ru.forumcalendar.forumcalendar.model.TeamModel;
import ru.forumcalendar.forumcalendar.model.form.TeamForm;
import ru.forumcalendar.forumcalendar.repository.TeamRepository;
import ru.forumcalendar.forumcalendar.repository.TeamRoleRepository;
import ru.forumcalendar.forumcalendar.repository.UserTeamRepository;
import ru.forumcalendar.forumcalendar.service.ShiftService;
import ru.forumcalendar.forumcalendar.service.TeamService;
import ru.forumcalendar.forumcalendar.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class BaseTeamService implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamRoleRepository teamRoleRepository;
    private final UserTeamRepository userTeamRepository;

    private final ShiftService shiftService;
    private final UserService userService;
    private final ConversionService conversionService;

    @Autowired
    public BaseTeamService(
            TeamRepository teamRepository,
            TeamRoleRepository teamRoleRepository,
            UserTeamRepository userTeamRepository,
            ShiftService shiftService,
            UserService userService,
            @Qualifier("mvcConversionService") ConversionService conversionService
    ) {
        this.teamRepository = teamRepository;
        this.teamRoleRepository = teamRoleRepository;
        this.userTeamRepository = userTeamRepository;
        this.shiftService = shiftService;
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
    public List<TeamModel> getAll() {
        return teamRepository.findAll()
                .stream()
                .map((t) -> conversionService.convert(t, TeamModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Team save(TeamForm teamForm) {

        Team team = teamRepository.findById(teamForm.getId()).orElse(new Team());
//        team.setUser(userService.getCurrentUser());
        team.setName(teamForm.getName());
        team.setDirection(teamForm.getDirection());
        team.setDescription(teamForm.getDescription());
        team.setShift(shiftService.get(teamForm.getShiftId()));

        return teamRepository.save(team);
    }

    @Override
    public Team delete(int id) {
        Team team = get(id);
        teamRepository.deleteById(id);
        return team;
    }

    @Override
    public Team updateTeamName(int teamId, String name) {
        Team team = get(teamId);
        team.setName(name);
        return teamRepository.save(team);
    }

    @Override
    public List<TeamMemberModel> getTeamMembers(int teamId) {
        Team team = get(teamId);
        List<TeamMemberModel> users = new ArrayList<>();
        for (UserTeam userTeam : team.getUserTeams()) {
            TeamMemberModel teamMemberModel = new TeamMemberModel();
            teamMemberModel.setId(userTeam.getUserTeamIdentity().getUser().getId());
            teamMemberModel.setFirstName(userTeam.getUserTeamIdentity().getUser().getFirstName());
            teamMemberModel.setLastName(userTeam.getUserTeamIdentity().getUser().getLastName());
            teamMemberModel.setPhoto(userTeam.getUserTeamIdentity().getUser().getPhoto());
            teamMemberModel.setTeamRole(userTeam.getTeamRole().getName());
            users.add(teamMemberModel);
        }
        return users;
    }

    @Override
    public boolean becomeCaptainToggle(String userId, int teamId) {

        UserTeam userTeam = userTeamRepository.getByUserIdAndTeamId(userId, teamId);
        TeamRole captainRole = teamRoleRepository.getById(TeamRole.ROLE_CAPTAIN_ID);
        TeamRole memberRole = teamRoleRepository.getById(TeamRole.ROLE_MEMBER_ID);

        long captainCount = get(teamId)
                .getUserTeams()
                .stream()
                .filter(u -> u.getTeamRole().getName().equals(captainRole.getName()))
                .count();

        if (captainCount == 0) {
            switch (userTeam.getTeamRole().getId()) {
                case TeamRole.ROLE_MEMBER_ID: {
                    userTeam.setTeamRole(captainRole);
                    userTeamRepository.save(userTeam);
                    return true;
                }
                case TeamRole.ROLE_CAPTAIN_ID: {
                    userTeam.setTeamRole(memberRole);
                    userTeamRepository.save(userTeam);
                    break;
                }
                default:
                    break;
            }
        }
        return false;
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
                .map((t) -> conversionService.convert(t, TeamModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamModel> getTeamModelsWithoutCuratorByShiftId(int id) {
        return teamRepository.getAllByShiftIdAndWithoutCurator(id)
                .map((t) -> conversionService.convert(t, TeamModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, String> getTeamIdNameMapByShiftId(int id) {
        return teamRepository.getAllByShiftIdOrderByCreatedAt(id)
                .collect(Collectors.toMap(Team::getId, Team::getName));
    }

    @Override
    public boolean hasPermissionToWrite(int id) {
        return shiftService.hasPermissionToWrite(get(id).getShift().getId());
    }

    @Override
    public boolean hasPermissionToRead(int id) {
        return true;
    }

//    @Override
//    public boolean isUserTeam(int id) {
//        return get(id).getUser().getId().equals(userService.getCurrentId());
//    }
}
