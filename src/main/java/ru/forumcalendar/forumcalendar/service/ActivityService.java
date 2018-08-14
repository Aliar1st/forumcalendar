package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.domain.Like;
import ru.forumcalendar.forumcalendar.model.ActivityModel;
import ru.forumcalendar.forumcalendar.model.form.ActivityForm;

import java.util.List;

public interface ActivityService extends SecuredService {

    boolean exist(int id);

    Activity get(int id);

    List<ActivityModel> getAll();

    Activity save(ActivityForm activityForm);

    void delete(int id);

    List<ActivityModel> getCurrentUserActivityModels();
}
