package ru.forumcalendar.forumcalendar.service;

public interface SecuredService {

    boolean hasPermissionToWrite(int id);

    boolean hasPermissionToRead(int id);
}
