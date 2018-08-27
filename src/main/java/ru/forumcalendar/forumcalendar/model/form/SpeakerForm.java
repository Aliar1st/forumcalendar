package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import ru.forumcalendar.forumcalendar.domain.Speaker;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class SpeakerForm {

    private int id;

    private String photoUrl = "photo-ava.jpg";

    private MultipartFile photo;

    //@ActivityExist
    private int activityId;

    @Size(min = 2, message = "Name is too short")
    @Size(max = 100, message = "Name is too long")
    @Pattern(regexp = "([A-ZА-Я][A-Za-zА-Яа-я0-9]+\\s?)+", message = "First name contains invalid characters")
    private String firstName;

    @Size(min = 2, message = "Name is too short")
    @Size(max = 50, message = "Name is too long")
    @Pattern(regexp = "[A-ZА-Я][A-Za-zА-Яа-я]+", message = "Last name contains invalid characters")
    private String lastName;

//    @Size(max = 2000, message = "Description is too long")
//    @Pattern(regexp = "(https?://)?(www\\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_+.~#?&//=]*)", message = "Invalid link (example: https://regexr.com/)")
    private String link;

    @Size(min = 2, message = "Description is too short")
    @Size(max = 5000, message = "Description is too long")
    private String description;

    public SpeakerForm(Speaker speaker) {
        this.id = speaker.getId();
        this.activityId = speaker.getActivity().getId();
        this.photoUrl = speaker.getPhoto();
        this.firstName = speaker.getFirstName();
        this.lastName = speaker.getLastName();
        this.link = speaker.getLink();
        this.description = speaker.getDescription();
    }
}
