package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.ContactType;

import java.util.List;

public interface ContactTypeService {

    boolean exist(int id);

    List<ContactType> getAll();
}
