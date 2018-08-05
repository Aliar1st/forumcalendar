package ru.forumcalendar.forumcalendar.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.forumcalendar.forumcalendar.domain.User;

import java.util.Map;

public interface UserService {

    User signUp(Map<String, Object> userMap);

    User getCurrentUser();
//
//    boolean hasRole(String role);
}
