package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserForm {

    private MultipartFile photo;

    @Length(max = 50, message = "First name is too long")
    @Pattern(regexp = "[A-ZА-Я][A-Za-zА-Яа-я]+", message = "First name is too short or contains invalid characters")
    private String firstName;

    @Length(max = 50, message = "Last name is too long")
    @Pattern(regexp = "[A-ZА-Я][A-Za-zА-Яа-я]+", message = "Last name is too short or contains invalid characters")
    private String lastName;
}
