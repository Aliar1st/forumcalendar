package ru.forumcalendar.forumcalendar.validation;

import org.springframework.beans.BeanWrapperImpl;
import ru.forumcalendar.forumcalendar.validation.annotation.DateTimeOrder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String dateTimeBeforeValue = new BeanWrapperImpl(value).getPropertyValue(dateTimeBefore).toString();
        String dateTimeAfterValue = new BeanWrapperImpl(value).getPropertyValue(dateTimeAfter).toString();

        try {
            LocalDateTime localDateBefore = LocalDateTime.parse(dateTimeBeforeValue, formatter);
            LocalDateTime localDateAfter = LocalDateTime.parse(dateTimeAfterValue, formatter);

            return localDateBefore.isBefore(localDateAfter);
        } catch (Exception e) {
            return false;
        }
    }
}