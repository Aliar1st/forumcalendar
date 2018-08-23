package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Event;
import ru.forumcalendar.forumcalendar.model.ShiftEventModel;
import ru.forumcalendar.forumcalendar.model.form.EventForm;

import java.time.LocalDate;
import java.util.List;

public interface EventService extends SecuredService, ResourceService<Event, ShiftEventModel, EventForm> {

    List<ShiftEventModel> getEventModelsByShiftIdAndDate(int shiftId, LocalDate date);

    List<ShiftEventModel> getEventModelsByShiftId(int shiftId);

    List<ShiftEventModel> getEventModelsByActivityId(int activityId);

    List<ShiftEventModel> getEventsBySpeakerId(int id);
}
