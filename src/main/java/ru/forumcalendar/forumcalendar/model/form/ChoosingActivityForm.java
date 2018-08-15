package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.Setter;
import ru.forumcalendar.forumcalendar.validation.annotation.ActivityExist;

@Getter
@Setter
public class ChoosingActivityForm {

    @ActivityExist
    private int activityId;
}
