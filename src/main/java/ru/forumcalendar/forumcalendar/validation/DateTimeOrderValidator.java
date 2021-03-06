package ru.forumcalendar.forumcalendar.validation;

import org.springframework.beans.BeanWrapperImpl;
import ru.forumcalendar.forumcalendar.validation.annotation.DateTimeOrder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeOrderValidator implements ConstraintValidator<DateTimeOrder, Object> {

    private String dateTimeBefore;
    private String dateTimeAfter;

    @Override
    public void initialize(DateTimeOrder constraint) {
        this.dateTimeBefore = constraint.dateTimeBefore();
        this.dateTimeAfter = constraint.dateTimeAfter();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        LocalDate dateTimeBeforeValue = (LocalDate) new BeanWrapperImpl(value).getPropertyValue(dateTimeBefore);
        LocalDate dateTimeAfterValue = (LocalDate) new BeanWrapperImpl(value).getPropertyValue(dateTimeAfter);

        return dateTimeBeforeValue != null
            && dateTimeAfterValue != null
            && dateTimeBeforeValue.isBefore(dateTimeAfterValue);
    }
}