package ru.forumcalendar.forumcalendar.service;

public interface LinkService {

    String generateLink(int teamId, int roleId);

    void inviteViaLink(String uniqueParam);
}
