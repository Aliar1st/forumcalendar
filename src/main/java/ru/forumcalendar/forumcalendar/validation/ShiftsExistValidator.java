package ru.forumcalendar.forumcalendar.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.forumcalendar.forumcalendar.service.ShiftService;
import ru.forumcalendar.forumcalendar.service.SpeakerService;
import ru.forumcalendar.forumcalendar.validation.annotation.ShiftsExist;
import ru.forumcalendar.forumcalendar.validation.annotation.SpeakersExist;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ShiftsExistValidator implements ConstraintValidator<ShiftsExist, List<Integer>> {

    private final ShiftService shiftService;

    @Autowired
    public ShiftsExistValidator(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @Override
    public boolean isValid(List<Integer> shiftsId, ConstraintValidatorContext constraintValidatorContext) {
        if (shiftsId != null) {
            for (int id : shiftsId) {
                if (!shiftService.exist(id)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
