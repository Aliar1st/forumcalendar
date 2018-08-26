package ru.forumcalendar.forumcalendar.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.domain.ActivityModerator;
import ru.forumcalendar.forumcalendar.domain.Role;
import ru.forumcalendar.forumcalendar.domain.User;
import ru.forumcalendar.forumcalendar.repository.ActivityModeratorRepository;
import ru.forumcalendar.forumcalendar.service.ActivityService;
import ru.forumcalendar.forumcalendar.service.PermissionService;
import ru.forumcalendar.forumcalendar.service.UserService;

@Service
@Transactional
public class BasePermissionService implements PermissionService {

    private final ActivityService activityService;
    private final UserService userService;

    private final ActivityModeratorRepository activityModeratorRepository;


    @Autowired
    public BasePermissionService(
            ActivityService activityService,
            UserService userService,
            ActivityModeratorRepository activityModeratorRepository
    ) {
        this.activityService = activityService;
        this.userService = userService;
        this.activityModeratorRepository = activityModeratorRepository;
    }

    @Override
    public Identifier identifyUser(int activityId) {
        Activity activity = activityService.get(activityId);
        User currentUser = userService.getCurrentUser();
        ActivityModerator activityModerator =
                activityModeratorRepository.getByUserIdAndActivityId(currentUser.getId(), activityId);

        if (currentUser.getRole().getId() == Role.ROLE_SUPERUSER_ID) {
            return Identifier.ADMIN;
        } else if (activity.getUser().getId().equals(userService.getCurrentId())
                || activityModerator != null) {
            return Identifier.MODERATOR;
        } else {
            return Identifier.USER;
        }
    }

    @Override
    public Identifier identifyUser() {
        User currentUser = userService.getCurrentUser();

        if (currentUser.getRole().getId() == Role.ROLE_SUPERUSER_ID) {
            return Identifier.ADMIN;
        } else {
            return Identifier.USER;
        }
    }


//    @Override
//    public Identifier identifyUser(Authentication auth, int id, String type, String perm) {
//        int userRoleId = userService.getCurrentUser().getRole().getId();
//        if (permissionEvaluator.hasPermission(auth, id, type, perm) &&
//                userRoleId == Role.ROLE_USER_ID) {
//            res = "isModerator";
//        } else if (userRoleId == Role.ROLE_USER_ID) {
//            res = "isJustUser";
//        } else {
//            res = "isAdmin";
//        }
//        return res;
//    }


}