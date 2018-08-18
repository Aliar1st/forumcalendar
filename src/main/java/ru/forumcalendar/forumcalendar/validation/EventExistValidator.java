package ru.forumcalendar.forumcalendar.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.forumcalendar.forumcalendar.service.EventService;
import ru.forumcalendar.forumcalendar.validation.annotation.EventExist;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EventExistValidator implements ConstraintValidator<EventExist, Integer> {

    private final EventService eventService;

    @Autowired
    public EventExistValidator(EventService eventService) {
        this.eventService = eventService;
    }

    public boolean isValid(Integer eventId, ConstraintValidatorContext context) {
        return eventService.exist(eventId);
    }
}
