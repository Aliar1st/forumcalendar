package ru.forumcalendar.forumcalendar.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.forumcalendar.forumcalendar.service.SpeakerService;
import ru.forumcalendar.forumcalendar.validation.annotation.SpeakersExist;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SpeakersValidator implements
        ConstraintValidator<SpeakersExist, Integer[]> {

    private final SpeakerService speakerService;

    @Autowired
    public SpeakersValidator(SpeakerService speakerService) {
        this.speakerService = speakerService;
    }

    @Override
    public boolean isValid(Integer[] speakersId, ConstraintValidatorContext constraintValidatorContext) {
        if (speakersId != null) {
            for (int id : speakersId) {
                if (!speakerService.exist(id)) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }
}
