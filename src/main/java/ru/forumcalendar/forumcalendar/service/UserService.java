package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.User;
import ru.forumcalendar.forumcalendar.model.UserModel;
import ru.forumcalendar.forumcalendar.model.form.UserForm;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UserService {

    List<UserModel> searchByName(String q, int activityId) throws InterruptedException;

    boolean exist(String id);

    User signUp(Map<String, Object> userMap);

    User getCurrentUser(Principal principal);

    User getCurrentUser();

    String getCurrentId();

    boolean isCurrentSuperUser();

    User get(String id);

    User save(UserForm userForm);

    List<UserModel> getAllNotCuratorsByShiftId(int shiftId);

    List<UserModel> getAllNotCuratorsByActivityId(int activityId);

    List<UserModel> getAllCuratorsByShiftId(int shiftId);

    List<UserModel> getAllCuratorsByActivityId(int activityId);

//    User get(String id);
//
//    User edit(String id, User user);

//    boolean hasRole(String role);
}
