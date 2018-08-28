package ru.forumcalendar.forumcalendar.validation.annotation;

import ru.forumcalendar.forumcalendar.validation.ShiftsExistValidator;
import ru.forumcalendar.forumcalendar.validation.SpeakersExistValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ShiftsExistValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ShiftsExist {

    String message() default "Events does not exist or nothing select";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
