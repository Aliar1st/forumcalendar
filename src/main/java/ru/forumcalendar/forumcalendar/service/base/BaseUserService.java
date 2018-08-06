package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.Role;
import ru.forumcalendar.forumcalendar.domain.User;
import ru.forumcalendar.forumcalendar.model.form.UserForm;
import ru.forumcalendar.forumcalendar.repository.RoleRepository;
import ru.forumcalendar.forumcalendar.repository.UserRepository;
import ru.forumcalendar.forumcalendar.service.UploadsService;
import ru.forumcalendar.forumcalendar.service.UserService;

import java.io.File;
import java.util.Map;

@Service
public class BaseUserService implements UserService {

    private static int ROLE_USER_ID = 1;
    private static int ROLE_ADMIN_ID = 2;

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private UploadsService uploadsService;

    @Autowired
    public BaseUserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UploadsService uploadsService) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    @Override
    public User save(UserForm userForm) {

        User user = userRepository.findById(userForm.getId())
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userForm.getId() + " can't be found"));

        String photo = uploadsService.upload(userForm.getPhoto(), userForm.getId())
                .map(File::getName)
                .orElse(user.getPhoto());

        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());

        user.setPhoto(photo);

        return userRepository.save(user);
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
