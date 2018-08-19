package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Team;

public interface LinkService {

    String generateLink(int teamId, int roleId);

    Team inviteViaLink(String uniqueParam);
}
