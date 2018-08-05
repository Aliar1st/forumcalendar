package ru.forumcalendar.forumcalendar.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.forumcalendar.forumcalendar.domain.User;

public interface UserService extends UserDetailsService {

    User signUp(User user);

    User getCurrentUser();

    boolean hasRole(String role);
}
