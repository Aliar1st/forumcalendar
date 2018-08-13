package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.model.EventModel;
import ru.forumcalendar.forumcalendar.model.form.EventForm;

import java.util.List;

public interface EventService {// extends ResourceService<Event, EventForm> {

    boolean exist(int id);

    Event get(int id);

    Event save(EventForm eventForm);

    void delete(int id);

    List<EventModel> getEventModelsByShiftId(int shiftId);

    boolean hasPermissionToWrite(int id);
}
