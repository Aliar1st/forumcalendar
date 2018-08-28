package ru.forumcalendar.forumcalendar.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.forumcalendar.forumcalendar.service.ShiftService;
import ru.forumcalendar.forumcalendar.validation.annotation.ShiftExist;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ShiftValidator implements ConstraintValidator<ShiftExist, Integer> {

    private final ShiftService shiftService;

    @Autowired
    public ShiftValidator(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @Override
    public boolean isValid(Integer shiftId, ConstraintValidatorContext constraintValidatorContext) {
        return shiftId == null || shiftService.exist(shiftId);
    }
}
