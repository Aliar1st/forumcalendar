package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.User;
import ru.forumcalendar.forumcalendar.model.form.UserForm;

import javax.jws.soap.SOAPBinding;
import java.util.Map;

public interface UserService {

    User signUp(Map<String, Object> userMap);

    User getCurrentUser();

    User save(UserForm userForm);

//    User get(String id);
//
//    User edit(String id, User user);

//    boolean hasRole(String role);
}
