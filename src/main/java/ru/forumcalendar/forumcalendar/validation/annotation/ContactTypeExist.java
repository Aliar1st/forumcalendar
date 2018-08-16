package ru.forumcalendar.forumcalendar.validation.annotation;

import ru.forumcalendar.forumcalendar.validation.ActivityExistValidator;
import ru.forumcalendar.forumcalendar.validation.ContactTypeExistValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ContactTypeExistValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContactTypeExist {

    String message() default "Activity does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
