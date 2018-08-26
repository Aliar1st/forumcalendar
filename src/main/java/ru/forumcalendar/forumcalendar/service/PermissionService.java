package ru.forumcalendar.forumcalendar.service;

public interface PermissionService {

    Identifier identifyUser(int activityId);

    Identifier identifyUser();

    enum Identifier {
        ADMIN,
        MODERATOR,
        USER;

        public String getValue() {
            switch (this) {
                case ADMIN: {
                    return "isAdmin";
                }
                case MODERATOR: {
                    return "isModerator";
                }
                default:
                    return "isJustUser";
            }
        }
    }
}
