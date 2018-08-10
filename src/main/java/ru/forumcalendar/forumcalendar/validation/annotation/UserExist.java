package ru.forumcalendar.forumcalendar.validation.annotation;

import ru.forumcalendar.forumcalendar.validation.UserValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserExist {
    String message() default "User does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
