package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.*;
import ru.forumcalendar.forumcalendar.repository.LinkRepository;
import ru.forumcalendar.forumcalendar.repository.TeamRepository;
import ru.forumcalendar.forumcalendar.repository.TeamRoleRepository;
import ru.forumcalendar.forumcalendar.repository.UserTeamRepository;
import ru.forumcalendar.forumcalendar.service.TeamService;
import ru.forumcalendar.forumcalendar.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Service
public class BaseTeamService implements TeamService {

    @Value("${my.inviteUrl}")
    private String INVITE_URL;

    private static int LINK_DELAY = 3600000; // 1 hour

    private UserService userService;
    private TeamRepository teamRepository;
    private LinkRepository linkRepository;
    private UserTeamRepository userTeamRepository;
    private TeamRoleRepository teamRoleRepository;

    @Autowired
    public BaseTeamService(
            UserService userService,
            TeamRepository teamRepository,
            LinkRepository linkRepository,
            UserTeamRepository userTeamRepository,
            TeamRoleRepository teamRoleRepository
    ) {
        this.userService = userService;
        this.teamRepository = teamRepository;
        this.linkRepository = linkRepository;
        this.userTeamRepository = userTeamRepository;
        this.teamRoleRepository = teamRoleRepository;
    }

    @Override
    public List<TeamRole> getAllRoles() {
        return teamRoleRepository.findAll();
    }

    @Override
    public String generateLink(
            @NotNull int teamId,
            @NotNull int roleId
    ) {
        Link link = new Link();
        String uniqueParam = UUID.randomUUID().toString().replace("-", "");
        TeamRole teamRole = teamRoleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Can't find team role with id '" + roleId + "'")
                );

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Can't find team with id '" + teamId + "'")
                );

        link.setLink(uniqueParam);
        link.setTeam(team);
        link.setTeamRole(teamRole);
        linkRepository.save(link);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                linkRepository.delete(link);
            }
        }, LINK_DELAY);

        return INVITE_URL + uniqueParam;
    }

    @Override
    public void inviteViaLink(String uniqueParam) {
        Link link = linkRepository.findById(uniqueParam)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Can't find link with id '" + uniqueParam)
                );

        User user = userService.getCurrentUser();
        UserTeam userTeam = new UserTeam();
        UserTeamIdentity userTeamIdentity = new UserTeamIdentity();

        userTeamIdentity.setTeam(link.getTeam());
        userTeamIdentity.setUser(user);

        userTeam.setTeamRole(link.getTeamRole());
        userTeam.setUserTeamIdentity(userTeamIdentity);

        userTeamRepository.save(userTeam);
    }
}
