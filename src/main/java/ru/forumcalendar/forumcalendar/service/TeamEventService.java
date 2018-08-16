package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.TeamEvent;
import ru.forumcalendar.forumcalendar.model.TeamEventModel;
import ru.forumcalendar.forumcalendar.model.form.TeamEventForm;

public interface TeamEventService extends SecuredService, ResourceService<TeamEvent, TeamEventModel, TeamEventForm> {
}
