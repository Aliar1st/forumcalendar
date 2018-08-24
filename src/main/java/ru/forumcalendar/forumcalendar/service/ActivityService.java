package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.domain.Like;
import ru.forumcalendar.forumcalendar.model.ActivityModel;
import ru.forumcalendar.forumcalendar.model.TeamModel;
import ru.forumcalendar.forumcalendar.model.form.ActivityForm;

import java.util.List;

public interface ActivityService extends SecuredService, ResourceService<Activity, ActivityModel, ActivityForm> {

    List<ActivityModel> searchByName(String q) throws InterruptedException;

    List<ActivityModel> getCurrentUserActivityModels();
}
