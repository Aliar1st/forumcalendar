package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.TeamEvent;
import ru.forumcalendar.forumcalendar.model.TeamEventModel;
import ru.forumcalendar.forumcalendar.model.form.TeamEventForm;

import java.time.LocalDate;
import java.util.List;

public interface TeamEventService extends SecuredService, ResourceService<TeamEvent, TeamEventModel, TeamEventForm> {

    List<TeamEventModel> getTeamEventModelsByTeamIdAndDate(int teamId, LocalDate date);

    List<TeamEventModel> getTeamEventModelsByTeamId(int teamId);
}
