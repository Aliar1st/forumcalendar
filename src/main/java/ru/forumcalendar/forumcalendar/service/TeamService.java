package ru.forumcalendar.forumcalendar.service;

import org.springframework.beans.factory.annotation.Value;
import ru.forumcalendar.forumcalendar.domain.TeamRole;

import java.util.List;

public interface TeamService {

    List<TeamRole> getAllRoles();

    String generateLink(int teamId, int roleId);

    void inviteViaLink(String uniqueParam);
}
