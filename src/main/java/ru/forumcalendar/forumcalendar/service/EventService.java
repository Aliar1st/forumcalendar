package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.model.EventModel;
import ru.forumcalendar.forumcalendar.model.form.EventForm;

import java.util.List;

public interface EventService extends SecuredService, ResourceService<Event, EventModel, EventForm> {

    List<EventModel> getEventModelsByShiftId(int shiftId);
}
