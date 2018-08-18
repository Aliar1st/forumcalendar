package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.forumcalendar.forumcalendar.domain.Role;
import ru.forumcalendar.forumcalendar.service.PermissionService;
import ru.forumcalendar.forumcalendar.service.UserService;

@Service
@Transactional
public class BasePermissionService implements PermissionService {

    private final UserService userService;

    private final PermissionEvaluator permissionEvaluator;

    @Autowired
    public BasePermissionService(
            UserService userService,
            PermissionEvaluator permissionEvaluator
    ) {
        this.userService = userService;
        this.permissionEvaluator = permissionEvaluator;
    }

    @Override
    public String identifyUser(Authentication auth, int id, String type, String perm) {
        String res = "";
        int userRoleId = userService.getCurrentUser().getRole().getId();
        if (permissionEvaluator.hasPermission(auth, id, type, perm) &&
                userRoleId == Role.ROLE_USER_ID) {
            res = "isModerator";
        } else if (userRoleId == Role.ROLE_USER_ID) {
            res = "isJustUser";
        } else {
            res = "isAdmin";
        }
        return res;
    }
}