package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.*;
import ru.forumcalendar.forumcalendar.exception.EntityNotFoundException;
import ru.forumcalendar.forumcalendar.model.form.UserForm;
import ru.forumcalendar.forumcalendar.repository.*;
import ru.forumcalendar.forumcalendar.service.UploadsService;
import ru.forumcalendar.forumcalendar.service.UserService;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Service
public class BaseUserService implements UserService {

    @Value("${my.inviteUrl}")
    private String INVITE_URL;

    private static int LINK_DELAY = 3600000; // 1 hour

    private static int ROLE_USER_ID = 1;
    private static int ROLE_ADMIN_ID = 2;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private TeamRoleRepository teamRoleRepository;
    private TeamRepository teamRepository;
    private UserTeamRepository userTeamRepository;
    private LinkRepository linkRepository;

    private UploadsService uploadsService;

    @Autowired
    public BaseUserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            TeamRoleRepository teamRoleRepository,
            TeamRepository teamRepository,
            UserTeamRepository userTeamRepository,
            LinkRepository linkRepository,
            UploadsService uploadsService
    ) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.teamRoleRepository = teamRoleRepository;
        this.teamRepository = teamRepository;
        this.userTeamRepository = userTeamRepository;
        this.linkRepository = linkRepository;
        this.uploadsService = uploadsService;
    }

    //
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        User user = userRepository.findByLogin(username);
//
//        if (Objects.isNull(user)) {
//            throw new UsernameNotFoundException("Can't find user with username " + username);
//        }
//
//        return (UserDetails) user;
//    }
//
//
    @Override
    public User signUp(Map<String, Object> userMap) {

        User user = new User();

        Role roleUser = roleRepository.findById(ROLE_USER_ID)
                .orElseThrow(() -> new IllegalArgumentException("Can't find role with id " + ROLE_USER_ID));

        user.setRole(roleUser);
        user.setId((String) userMap.get("sub"));
        user.setFirstName((String) userMap.get("given_name"));
        user.setLastName((String) userMap.get("family_name"));
        user.setEmail(userMap.get("email").toString());
        user.setGender((String) userMap.get("gender"));
        user.setLocale((String) userMap.get("locale"));


        String photo = uploadsService.upload((String) userMap.get("picture"), user.getId())
                .map(File::getName)
                .orElse("DEFAULT");

        user.setPhoto(photo);

        return userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with id " + user.getId() + " not found"));
    }

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));
    }

    @Override
    public User save(UserForm userForm) {

        User user = userRepository.findById(userForm.getId())
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userForm.getId() + " not found"));

        String photo = uploadsService.upload(userForm.getPhoto(), userForm.getId())
                .map((f) -> {
                    if (!user.getPhoto().equals(f.getName()))
                        uploadsService.delete(user.getPhoto());

                    return f.getName();
                })
                .orElse(user.getPhoto());

        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());

        user.setPhoto(photo);

        return userRepository.save(user);
    }

    @Override
    public String generateLink(
            @NotNull int teamId,
            @NotNull int roleId
    ) {
        Link link = new Link();
        String uniqueParam = UUID.randomUUID().toString().replace("-","");
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

        User user = getCurrentUser();
        UserTeam userTeam = new UserTeam();
        UserTeamIdentity userTeamIdentity = new UserTeamIdentity();

        userTeamIdentity.setTeam(link.getTeam());
        userTeamIdentity.setUser(user);

        userTeam.setTeamRole(link.getTeamRole());
        userTeam.setUserTeamIdentity(userTeamIdentity);

        userTeamRepository.save(userTeam);
        linkRepository.delete(link);
    }

//
//    @Override
//    public boolean hasRole(String role) {
//
//        //noinspection unchecked
//        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext()
//                .getAuthentication().getAuthorities();
//
//        for (GrantedAuthority authority : authorities) {
//            if (authority.getAuthority().equals(role)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
}
