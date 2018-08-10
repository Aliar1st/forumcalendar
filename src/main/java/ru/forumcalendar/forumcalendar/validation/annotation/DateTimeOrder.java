package ru.forumcalendar.forumcalendar.validation.annotation;

import ru.forumcalendar.forumcalendar.validation.DateTimeOrderValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateTimeOrderValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeOrder {

    String message() default "Invalid date order";

    String dateTimeBefore();

    String dateTimeAfter();

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        DateTimeOrder[] value();
    }

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
