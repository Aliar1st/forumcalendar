package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.User;
import ru.forumcalendar.forumcalendar.model.form.UserForm;

import java.util.Map;

public interface UserService {

    boolean exist(String id);

    User signUp(Map<String, Object> userMap);

    User getCurrentUser();

    String getCurrentId();

    User get(String id);

    User save(UserForm userForm);

//    User get(String id);
//
//    User edit(String id, User user);

//    boolean hasRole(String role);
}
