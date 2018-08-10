package ru.forumcalendar.forumcalendar.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.forumcalendar.forumcalendar.service.ActivityService;
import ru.forumcalendar.forumcalendar.validation.annotation.ActivityExist;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ActivityExistValidator implements
        ConstraintValidator<ActivityExist, Integer> {

    private final ActivityService activityService;

    @Autowired
    public ActivityExistValidator(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Override
    public boolean isValid(Integer activityId, ConstraintValidatorContext cvc) {
        return activityService.exist(activityId);
    }
}
