package ru.forumcalendar.forumcalendar.service;

import ru.forumcalendar.forumcalendar.domain.Activity;
import ru.forumcalendar.forumcalendar.model.ActivityModel;
import ru.forumcalendar.forumcalendar.model.form.ActivityForm;

import java.util.List;

public interface ActivityService {

    Activity get(int id);

    Activity save(ActivityForm activityForm);

    void delete(int id);

    List<ActivityModel> getCurrentUserActivityModels();
}
