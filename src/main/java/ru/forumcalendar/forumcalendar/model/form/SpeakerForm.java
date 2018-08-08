package ru.forumcalendar.forumcalendar.model.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.forumcalendar.forumcalendar.domain.Speaker;

@Getter
@Setter
@NoArgsConstructor
public class SpeakerForm {

    private int id = -1;

    private int activityId;

    private String firstName;

    private String lastName;

    private String link;

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
