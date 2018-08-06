package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserForm {

    private String id;

    private MultipartFile photo;

    private String firstName;

    private String lastName;
}
