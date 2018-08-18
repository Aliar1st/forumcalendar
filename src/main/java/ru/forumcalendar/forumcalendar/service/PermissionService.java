package ru.forumcalendar.forumcalendar.service;

import org.springframework.security.core.Authentication;

public interface PermissionService {

    String identifyUser(Authentication auth, int id, String type, String perm);
}
