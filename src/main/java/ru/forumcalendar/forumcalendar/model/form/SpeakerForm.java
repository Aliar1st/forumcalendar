package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.forumcalendar.forumcalendar.domain.Speaker;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class SpeakerForm {

    private int id;

    //@ActivityExist
    private int activityId;

    @Min(value = 2, message = "First name is too short")
    @Max(value = 50, message = "First name is too long")
    @Pattern(regexp = "[A-ZА-Я][A-Za-zА-Яа-я]+", message = "First name contains invalid characters")
    private String firstName;

    @Min(value = 2, message = "Last name is too short")
    @Max(value = 50, message = "Last name is too long")
    @Pattern(regexp = "[A-ZА-Я][A-Za-zА-Яа-я]+", message = "Last name contains invalid characters")
    private String lastName;

    @Max(value = 2000, message = "Link is too long")
    @Pattern(regexp = "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_+.~#?&//=]*)", message = "Invalid link (example: https://regexr.com/)")
    private String link;

    @Max(value = 5000, message = "Description is too long")
    private String description;

    public SpeakerForm(Speaker speaker) {
        this.id = speaker.getId();
        this.activityId = speaker.getActivity().getId();
        this.firstName = speaker.getFirstName();
        this.lastName = speaker.getLastName();
        this.link = speaker.getLink();
        this.description = speaker.getDescription();
    }
}
