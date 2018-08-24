package ru.forumcalendar.forumcalendar.validation.annotation;

import ru.forumcalendar.forumcalendar.validation.TeamValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TeamValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TeamExist {

    String message() default "Team does not exist... You aren't exist, your reality is illusion and your life is just a joke.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
