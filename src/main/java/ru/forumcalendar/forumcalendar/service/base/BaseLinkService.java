package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.forumcalendar.forumcalendar.domain.*;
import ru.forumcalendar.forumcalendar.repository.*;
import ru.forumcalendar.forumcalendar.service.LinkService;
import ru.forumcalendar.forumcalendar.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Service
@Transactional
public class BaseLinkService implements LinkService {

    @Value("${my.inviteUrl}")
    private String INVITE_URL;

    private static final int LINK_DELAY = 3600000; // 1 hour

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final UserService userService;
    private final TeamRepository teamRepository;
    private final LinkRepository linkRepository;
    private final UserTeamRepository userTeamRepository;
    private final TeamRoleRepository teamRoleRepository;

    @Autowired
    public BaseLinkService(
            RoleRepository roleRepository,
            UserRepository userRepository,
            UserService userService,
            TeamRepository teamRepository,
            LinkRepository linkRepository,
            UserTeamRepository userTeamRepository,
            TeamRoleRepository teamRoleRepository
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.teamRepository = teamRepository;
        this.linkRepository = linkRepository;
        this.userTeamRepository = userTeamRepository;
        this.teamRoleRepository = teamRoleRepository;
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

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                linkRepository.delete(link);
            }
        }, LINK_DELAY);

        return INVITE_URL + uniqueParam;
    }

    @Override
    public Team inviteViaLink(String uniqueParam) {
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

        Team team = userTeamRepository.save(userTeam).getUserTeamIdentity().getTeam();

        if (team.isAdmin()) {
            Role adminRole = roleRepository.findById(2).orElseThrow(() -> new IllegalArgumentException(
                    "Can't find role with id '" + 2)
            );
            user.setRole(adminRole);
            userRepository.save(user);
        }

        return team;
    }
}
