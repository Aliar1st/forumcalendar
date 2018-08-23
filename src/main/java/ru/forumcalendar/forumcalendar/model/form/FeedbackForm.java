package ru.forumcalendar.forumcalendar.model.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class FeedbackForm {

    private int id;

    @Size(min = 2, message = "Theme is too short")
    @Size(max = 50, message = "Theme is too long")
    @Pattern(regexp = "([A-Za-zА-Яа-я0-9]\\s?)+", message = "Theme contains invalid characters")
    private String theme;

    @Size(max = 5000, message = "Feedback is too long")
    private String feedback;
}
