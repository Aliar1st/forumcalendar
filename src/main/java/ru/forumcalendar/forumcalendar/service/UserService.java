package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.User;

import java.util.Map;

public interface UserService {

    User signUp(Map<String, Object> userMap);

    User getCurrentUser();

//    User get(String id);
//
//    User edit(String id, User user);



//    boolean hasRole(String role);
}
