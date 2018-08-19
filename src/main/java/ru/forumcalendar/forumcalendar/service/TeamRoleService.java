package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.TeamRole;

import java.util.List;
import java.util.stream.Stream;

public interface TeamRoleService {

    boolean isUserCuratorOfTeam(String userId, int teamId);

    boolean isUserCaptainOfTeam(String userId, int teamId);

    boolean isUserMemberOfTeam(String userId, int teamId);

    boolean isExistCurator(int teamId);

    boolean exist(int id);

    List<TeamRole> getAllRoles();
}
