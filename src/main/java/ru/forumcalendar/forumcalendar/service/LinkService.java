package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.TeamRole;

import java.util.List;

public interface LinkService {

    String generateLink(int teamId, int roleId);

    void inviteViaLink(String uniqueParam);
}
