package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.TeamRole;

import java.util.List;

public interface TeamRoleService {

    boolean exist(int id);

    List<TeamRole> getAllRoles();
}
