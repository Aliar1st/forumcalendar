package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.model.ActivityModel;
import ru.forumcalendar.forumcalendar.model.form.ActivityForm;
import ru.forumcalendar.forumcalendar.model.form.ResourceForm;

import java.util.List;

public interface ResourceService<T, F extends ResourceForm<T>> {

    boolean exist(int id);

    T get(int id);

    T save(F form);

    void delete(int id);

    boolean hasPermissionToWrite(int id);

    boolean hasPermissionToRead(Integer id);
}
