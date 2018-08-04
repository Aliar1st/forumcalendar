package ru.forumcalendar.forumcalendar.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.forumcalendar.forumcalendar.domain.Role;
import ru.forumcalendar.forumcalendar.domain.User;
import ru.forumcalendar.forumcalendar.repository.RoleRepository;
import ru.forumcalendar.forumcalendar.repository.UserRepository;
import ru.forumcalendar.forumcalendar.service.UserService;

import java.util.Collection;
import java.util.Objects;

@Service
public class BaseUserService implements UserService {

    private static int ROLE_USER_ID = 1;
    private static int ROLE_ADMIN_ID = 2;

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Autowired
    public BaseUserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByLogin(username);

        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("Can't find user with username " + username);
        }

        return user;
    }


    @Override
    public User signUpUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role roleUser = roleRepository.findById(ROLE_USER_ID)
                .orElseThrow(() -> new IllegalArgumentException("Can't find role with id " + ROLE_USER_ID));

        user.setRole(roleUser);

        return userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User user = null;

        if (userDetails instanceof User) {
            user = (User) userDetails;
        }

        return user;
    }

    @Override
    public boolean hasRole(String role) {

        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities();

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(role)) {
                return true;
            }
        }

        return false;
    }
}
