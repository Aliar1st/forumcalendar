package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.TeamRole;

import java.util.List;

public interface TeamRoleService {

    boolean isUserCuratorOfTeam(String userId, int teamId);

    boolean isUserCaptainOfTeam(String userId, int teamId);

    boolean isUserMemberOfTeam(String userId, int teamId);

    boolean isExistCurator(int teamId);

    Role getMemberRole(String userId, int teamId);

    boolean exist(int id);

    List<TeamRole> getAllRoles();

    enum Role {
        CURATOR,
        CAPTAIN,
        MEMBER,
        NOT_IN_TEAM;

        public String getValue() {
            switch (this) {
                case CURATOR: {
                    return "isCurator";
                }
                case CAPTAIN: {
                    return "isCaptain";
                }
                case MEMBER: {
                    return "isMember";
                }
                default:
                    return "NOT_IN_TEAM";
            }
        }
    }

}
