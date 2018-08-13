package ru.forumcalendar.forumcalendar.validation.annotation;

import ru.forumcalendar.forumcalendar.validation.ActivityExistValidator;
import ru.forumcalendar.forumcalendar.validation.TeamRoleExistValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TeamRoleExistValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TeamRoleExist {

    String message() default "Team role does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
