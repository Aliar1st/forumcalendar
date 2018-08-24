package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class FeedbackForm {

    private int id;

    private String userId;

    @Size(min = 2, message = "Title is too short")
    @Size(max = 50, message = "Title is too long")
    @Pattern(regexp = "([A-Za-zА-Яа-я0-9]\\s?)+", message = "Title contains invalid characters")
    private String title;

    @Size(min = 2, message = "Theme is too short")
    @Size(max = 50, message = "Theme is too long")
    @Pattern(regexp = "([A-Za-zА-Яа-я0-9]\\s?)+", message = "Theme contains invalid characters")
    private String theme;

    @Size(max = 5000, message = "Feedback is too long")
    private String feedback;
}
