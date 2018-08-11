package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.forumcalendar.forumcalendar.domain.Speaker;
import ru.forumcalendar.forumcalendar.validation.annotation.ActivityExist;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class SpeakerForm {

    private int id = -1;

    //@ActivityExist
    private int activityId;

    @Length(max = 50, message = "First name is too long")
    @Pattern(regexp = "[A-ZА-Я][A-Za-zА-Яа-я]+", message = "First name is too short or contains invalid characters")
    private String firstName;

    @Length(max = 50, message = "Last name is too long")
    @Pattern(regexp = "[A-ZА-Я][A-Za-zА-Яа-я]+", message = "Last name is too short or contains invalid characters")
    private String lastName;

    @Length(max = 2000, message = "Link is too long")
    @Pattern(regexp = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)", message = "Invalid link (example: https://regexr.com/)")
    private String link;

    @Length(max = 5000, message = "Description is too long")
    @Pattern(regexp = "[A-ZА-Я].+", message = "Description is too short or contains invalid characters")
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
